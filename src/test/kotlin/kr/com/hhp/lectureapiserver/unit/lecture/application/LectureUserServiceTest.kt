package kr.com.hhp.lectureapiserver.unit.lecture.application

import kr.com.hhp.lectureapiserver.lecture.application.LectureUserService
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.domain.LectureUserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any

@ExtendWith(MockitoExtension::class)
class LectureUserServiceTest {

    @Mock
    private lateinit var lectureUserRepository: LectureUserRepository

    @InjectMocks
    private lateinit var lectureUserService: LectureUserService


    @Nested
    @DisplayName("특강 신청 조회")
    inner class GetNullAbleLectureUserTest {
        @Test
        fun `성공 (특강 신청이 정상적으로 되어있는 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            given(lectureUserRepository.findByUserIdAndLectureId(userId, lectureId))
                .willReturn(
                    LectureUserEntity(userId, lectureId)
                )

            //when
            val lectureUser = lectureUserService.getNullAbleLectureUser(userId, lectureId)

            //then
            then(lectureUserRepository).should().findByUserIdAndLectureId(userId, lectureId)
            assertNotNull(lectureUser)
            assertEquals(userId, lectureUser?.userId)
            assertEquals(lectureId, lectureUser?.lectureId)
        }

        @Test
        fun `성공 (특강 신청이 되어있지 않은 경우)`() {
            //given
            val userId = 1L
            val lectureId = 1L
            given(lectureUserRepository.findByUserIdAndLectureId(userId, lectureId))
                .willReturn(null)

            //when
            val lectureUser = lectureUserService.getNullAbleLectureUser(userId, lectureId)

            //then
            then(lectureUserRepository).should().findByUserIdAndLectureId(userId, lectureId)
            assertNull(lectureUser)
        }
    }

    @Nested
    @DisplayName("특강에 등록된 학생 수 조회")
    inner class CountAllByLectureIdTest {
        @Test
        fun `성공 (특강에 등록된 학생이 10명인 경우)`() {
            //given
            val lectureId = 1L
            val expectedCount = 10L
            given(lectureUserRepository.countAllByLectureId(lectureId))
                .willReturn(expectedCount)

            //when
            val currentStudentCount = lectureUserService.countAllByLectureId(lectureId)

            //then
            then(lectureUserRepository).should().countAllByLectureId(lectureId)
            assertEquals(currentStudentCount, expectedCount)
        }
    }

    @Nested
    @DisplayName("특강에 등록")
    inner class SaveTest {
        @Test
        fun `성공 (특강에 등록)`() {
            //given
            val lectureUserId = 1L
            val userId = 1L
            val lectureId = 1L
            val expectedLectureUser = LectureUserEntity(lectureUserId = lectureUserId, userId = userId, lectureId = lectureId)
            given(lectureUserRepository.save(any())).willReturn(expectedLectureUser)

            //when
            val savedLectureUser = lectureUserService.save(userId = userId, lectureId = lectureId)

            //then
            then(lectureUserRepository).should().save(any())
            assertNotNull(savedLectureUser)
            assertEquals(expectedLectureUser, savedLectureUser)
        }
    }

}