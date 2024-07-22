package kr.com.hhp.lectureapiserver.unit.lecture.application

import kr.com.hhp.lectureapiserver.lecture.application.LectureEnrollmentHistoryService
import kr.com.hhp.lectureapiserver.lecture.domain.LectureEnrollmentHistoryRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given

@ExtendWith(MockitoExtension::class)
class LectureEnrollmentHistoryServiceTest {

    @Mock
    private lateinit var lectureEnrollmentHistoryRepository: LectureEnrollmentHistoryRepository

    @InjectMocks
    private lateinit var lectureEnrollmentHistoryService: LectureEnrollmentHistoryService

    @Test
    fun `특강 신청자 히스토리 저장`() {
        //given
        val lectureId = 1L
        val userId = 1L
        val isSuccessful = false

        val expectedLectureEnrollmentHistoryEntity = LectureEnrollmentHistoryEntity(
             userId = userId, lectureId = lectureId, isSuccessful = isSuccessful
        )
        given(lectureEnrollmentHistoryRepository.save(any()))
            .willReturn(expectedLectureEnrollmentHistoryEntity)

        //then
        val lectureEnrollmentHistoryEntity =
            lectureEnrollmentHistoryService.save(userId = userId, lectureId = lectureId, isSuccessful = isSuccessful)

        //then
        then(lectureEnrollmentHistoryRepository).should().save(any())
        assertNotNull(lectureEnrollmentHistoryEntity)
        assertEquals(lectureId, lectureEnrollmentHistoryEntity.lectureId)
        assertEquals(userId, lectureEnrollmentHistoryEntity.userId)
        assertEquals(isSuccessful, lectureEnrollmentHistoryEntity.isSuccessful)

    }
}