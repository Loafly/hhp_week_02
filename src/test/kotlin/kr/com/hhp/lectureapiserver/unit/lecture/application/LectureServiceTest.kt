package kr.com.hhp.lectureapiserver.unit.lecture.application

import kr.com.hhp.lectureapiserver.lecture.application.LectureEnrollmentHistoryService
import kr.com.hhp.lectureapiserver.lecture.application.LectureService
import kr.com.hhp.lectureapiserver.lecture.application.LectureUserService
import kr.com.hhp.lectureapiserver.lecture.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.exception.DuplicateApplicationException
import kr.com.hhp.lectureapiserver.lecture.exception.EarlyApplicationException
import kr.com.hhp.lectureapiserver.lecture.exception.LateApplicationException
import kr.com.hhp.lectureapiserver.lecture.exception.LectureNotFoundException
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
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
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class LectureServiceTest {

    @Mock
    private lateinit var lectureUserService: LectureUserService

    @Mock
    private lateinit var lectureRepository: LectureRepository

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
            val isSuccessful = true
            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(
                    LectureEntity(
                        lectureId, LocalDateTime.now(), 30, LocalDateTime.now(), null
                    )
                )
            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            //when
            lectureService.apply(userId, lectureId)

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
        }

        @Test
        fun `실패 (정원을 초과한 경우)` () {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(
                    LectureEntity(
                        lectureId, LocalDateTime.now(), 0, LocalDateTime.now(), null
                    )
                )

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            //when
            val exception = assertThrows<CapacityExceededException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            assertEquals("정원을 초과하였습니다.", exception.message)
        }

        @Test
        fun `실패 (특강 신청 가능 기간보다 이전인 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(
                    LectureEntity(
                        lectureId, LocalDateTime.now(), 30, LocalDateTime.now().plusDays(1L), null
                    )
                )

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            //when
            val exception = assertThrows<EarlyApplicationException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            assertEquals("특강 신청 기간이 아닙니다.", exception.message)
        }

        @Test
        fun `실패 (특강 신청 가능 기간보다 이후인 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false

            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(
                    LectureEntity(
                        lectureId, LocalDateTime.now(), 30, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1)
                    )
                )

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            //when
            val exception = assertThrows<LateApplicationException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureRepository).should().findByLectureId(lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            assertEquals("특강 신청 기간이 아닙니다.", exception.message)
        }

        @Test
        fun `실패 (동일한 특강 신청 완료가 되어있는 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            val isSuccessful = false

            given(lectureUserService.getNullAbleLectureUser(userId, lectureId))
                .willReturn(LectureUserEntity(lectureId = lectureId, userId = userId))

            given(lectureEnrollmentHistoryService.save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))
                .willReturn(LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful))

            //when
            val exception = assertThrows<DuplicateApplicationException> {
                lectureService.apply(userId, lectureId)
            }

            //then
            then(lectureUserService).should().getNullAbleLectureUser(userId, lectureId)
            then(lectureEnrollmentHistoryService).should().save(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
            assertEquals("동일한 특강에 신청완료된 내역이 있습니다. userId : $userId, lectureId : $lectureId", exception.message)

        }

    }

    @Nested
    @DisplayName("특강 조회")
    inner class GetByLectureIdTest {

        @Test
        fun `성공 (특강이 있는 경우)`() {
            //given
            val lectureId = 1L
            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(
                    LectureEntity(
                        lectureId, LocalDateTime.now(), 30, LocalDateTime.now(), null
                    )
                )

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
            given(lectureRepository.findByLectureId(lectureId))
                .willReturn(null)

            // when
            val exception = assertThrows<LectureNotFoundException> {
                lectureService.getByLectureId(lectureId)
            }

            then(lectureRepository).should().findByLectureId(lectureId)
            assertEquals("lecture가 존재하지 않습니다. lectureId : $lectureId", exception.message)
        }
    }

    @Nested
    @DisplayName("특강 생성")
    inner class SaveTest {
        @Test
        fun `성공 (모든 필드가 있는 특강 생성)`() {
            //given
            val lectureId = 1L
            val lectureDateTime = LocalDateTime.now()
            val capacity = 30
            val enrollmentPeriodStart = LocalDateTime.now()
            val enrollmentPeriodEnd = LocalDateTime.now()

            val expectedLectureEntity = LectureEntity(
                lectureId = lectureId,
                lectureDateTime = lectureDateTime,
                capacity = capacity,
                enrollmentPeriodStart = enrollmentPeriodStart,
                enrollmentPeriodEnd = enrollmentPeriodEnd
            )

            given(lectureRepository.save(any()))
                .willReturn(expectedLectureEntity)

            // when
            val savedLecture = lectureService.save(lectureDateTime, capacity, enrollmentPeriodStart, enrollmentPeriodEnd)

            //then
            then(lectureRepository).should().save(any())
            assertNotNull(savedLecture)
            assertEquals(expectedLectureEntity, savedLecture)
        }
    }
}