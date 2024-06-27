package kr.com.hhp.lectureapiserver.unit.lecture.application

import kr.com.hhp.lectureapiserver.lecture.application.LectureEnrollmentHistoryService
import kr.com.hhp.lectureapiserver.lecture.application.LectureService
import kr.com.hhp.lectureapiserver.lecture.application.LectureUserService
import kr.com.hhp.lectureapiserver.lecture.application.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.application.exception.DuplicateApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureDuplicateException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureNotFoundException
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
import kr.com.hhp.lectureapiserver.user.application.UserService
import kr.com.hhp.lectureapiserver.user.application.exception.UserNotFoundException
import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class LectureServiceTest {

    @Mock
    private lateinit var lectureUserService: LectureUserService

    @Mock
    private lateinit var lectureRepository: LectureRepository

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var lectureEnrollmentHistoryService: LectureEnrollmentHistoryService

    @InjectMocks
    private lateinit var lectureService: LectureService

    @Nested
    @DisplayName("특강 신청 여부 조회")
    inner class IsLectureEnrolledTest { // Is로 시작하는 class명이 맞는지 고민... (테스트코드이기에 함수명 뒤에 Test를 붙인 상태)
        @Test
        fun `특강신청이 되어 있는 경우`() {

            //given
            val userId = 1L
            val lectureId = 1L
            val lectureUserEntity = LectureUserEntity(userId, lectureId)

            given(lectureUserService.getNullAbleLectureUser(userId, lectureId)).willReturn(lectureUserEntity)

            //when
            val lectureEnrolled = lectureService.isLectureEnrolled(userId, lectureId)

            //then
            then(lectureUserService).should().getNullAbleLectureUser(userId, lectureId)
            assertNotNull(lectureEnrolled)
            assertTrue(lectureEnrolled)
        }

        @Test
        fun `특강신청이 되어있지 않은 경우`() {

            //given
            val userId = 1L
            val lectureId = 1L

            given(lectureUserService.getNullAbleLectureUser(userId, lectureId)).willReturn(null)

            //when
            val lectureEnrolled = lectureService.isLectureEnrolled(userId, lectureId)

            //then
            then(lectureUserService).should().getNullAbleLectureUser(userId, lectureId)
            assertNotNull(lectureEnrolled)
            assertFalse(lectureEnrolled)
        }
    }


    @Nested
    @DisplayName("특강 신청")
    inner class ApplyTest {

        @Test
        fun `성공 (특강 신청)` () {
            //given
            val userId = 1L
            val lectureId = 1L
            val capacity = 30
            val isSuccessful = true

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(LectureEntity(lectureId = lectureId, capacity = capacity))
            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
            given(userService.getByUserId(userId)).willReturn(UserEntity(userId))

            //when
            lectureService.apply(userId, lectureId)

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            then(userService).should().getByUserId(userId)
        }

        @Test
        fun `실패 (정원을 초과한 경우)` () {
            //given
            val userId = 1L
            val lectureId = 1L
            val capacity = 0
            val isSuccessful = false

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(LectureEntity(lectureId = lectureId, capacity = capacity))

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            given(userService.getByUserId(userId)).willReturn(UserEntity(userId))

            //when
            val exception = assertThrows<CapacityExceededException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            then(userService).should().getByUserId(userId)
            assertEquals("정원을 초과하였습니다.", exception.message)
        }


        @Test
        fun `실패 (동일한 특강 신청 완료가 되어있는 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            val capacity = 30
            val isSuccessful = false

            given(lectureUserService.getNullAbleLectureUser(userId = userId, lectureId = lectureId))
                .willReturn(LectureUserEntity(lectureId = lectureId, userId = userId))

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            given(userService.getByUserId(userId)).willReturn(UserEntity(userId))

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(LectureEntity(lectureId = lectureId, capacity = capacity))

            //when
            val exception = assertThrows<DuplicateApplicationException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureUserService).should().getNullAbleLectureUser(userId = userId, lectureId =  lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            then(userService).should().getByUserId(userId)
            then(lectureRepository).should().findByLectureId(lectureId)
            assertEquals("동일한 특강에 신청완료된 내역이 있습니다. userId : $userId, lectureId : $lectureId", exception.message)
        }

        @Test
        fun `실패 (유저가 존재하지 않는 경우)` () {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false
            val expectedErrorMessage = "User를 찾을 수 없습니다. userId : $userId"

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            given(userService.getByUserId(userId)).willThrow(UserNotFoundException(expectedErrorMessage))

            //when
            val exception = assertThrows<UserNotFoundException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            then(userService).should().getByUserId(userId)
            assertEquals(expectedErrorMessage, exception.message)
        }

        @Test
        fun `실패 (특강이 존재하지 않는 경우)` () {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false
            val expectedErrorMessage = "Lecture를 찾을 수 없습니다. lectureId : $lectureId"

            given(lectureRepository.findByLectureId(lectureId)).willReturn(null)

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            given(userService.getByUserId(userId)).willReturn(UserEntity(userId))

            //when
            val exception = assertThrows<LectureNotFoundException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            then(userService).should().getByUserId(userId)
            assertEquals(expectedErrorMessage, exception.message)
        }

    }

    @Nested
    @DisplayName("특강 조회")
    inner class GetByLectureIdTest {

        @Test
        fun `성공 (특강이 있는 경우)`() {
            //given
            val lectureId = 1L
            val capacity = 30
            given(lectureRepository.findByLectureId(lectureId)).willReturn(LectureEntity(lectureId, capacity))

            // when
            val lecture = lectureService.getByLectureId(lectureId)

            then(lectureRepository).should().findByLectureId(lectureId)
            assertNotNull(lecture)
            assertEquals(lectureId, lecture.lectureId)
        }

        @Test
        fun `실패 (특강이 없는 경우)`() {
            //given
            val lectureId = 1L
            given(lectureRepository.findByLectureId(lectureId)).willReturn(null)

            // when
            val exception = assertThrows<LectureNotFoundException> {
                lectureService.getByLectureId(lectureId)
            }

            then(lectureRepository).should().findByLectureId(lectureId)
            assertEquals("Lecture를 찾을 수 없습니다. lectureId : $lectureId", exception.message)
        }
    }

    @Nested
    @DisplayName("특강 생성")
    inner class SaveLectureTest {
        @Test
        fun `성공 (lectureId가 생성된게 없는 경우)`() {
            //given
            val lectureId = 1L
            val expectedLecture = LectureEntity(lectureId)

            given(lectureRepository.findByLectureId(lectureId)).willReturn(null)

            given(lectureRepository.save(any())).willReturn(expectedLecture)

            //when
            val user = lectureService.save(lectureId)

            //then
            then(lectureRepository).should().save(any())
            assertEquals(expectedLecture, user)
        }

        @Test
        fun `실패 (Lecture가 존재하지 않는 경우)`() {
            //given
            val lectureId = 1L
            val expectedUser = LectureEntity(lectureId)
            given(lectureRepository.findByLectureId(lectureId)).willReturn(expectedUser)

            //when
            val exception = assertThrows<LectureDuplicateException> {
                lectureService.save(lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            assertEquals("동일한Id를 가진 Lecture가 존재합니다. lectureId : $lectureId", exception.message)
        }
    }
}