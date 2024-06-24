package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.infra.repository.LectureUserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

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
        fun `특강 신청이 정상적으로 되어있는 경우`() {
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
        fun `특강 신청이 되어있지 않은 경우`() {
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


}