package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.infra.repository.LectureRepository
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
class LectureServiceTest {

    @Mock
    private lateinit var lectureUserService: LectureUserService

    @InjectMocks
    private lateinit var lectureService: LectureService

    @Nested
    @DisplayName("특강 신청 여부 조회")
    inner class IsLectureEnrolledTest { // Is로 시작하는 class명이 맞는지 고민... (테스트코드이기에 함수명 뒤에 Test를 붙인 상태)
        @Test
        fun `특강신청이 된 경우`() {

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
        fun `특강신청이 되지 않은 경우`() {

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
}