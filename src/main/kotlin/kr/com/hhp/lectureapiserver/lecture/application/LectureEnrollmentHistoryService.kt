package kr.com.hhp.lectureapiserver.lecture.application

import kr.com.hhp.lectureapiserver.lecture.domain.LectureEnrollmentHistoryRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class LectureEnrollmentHistoryService(private val lectureEnrollmentHistoryRepository: LectureEnrollmentHistoryRepository) {

    @Transactional(propagation = Propagation.REQUIRES_NEW) // History이기 때문에 다른 트랜잭션과 분리되어 실행
    fun save(lectureId: Long, userId: Long, isSuccessful: Boolean): LectureEnrollmentHistoryEntity {
        return lectureEnrollmentHistoryRepository.save(
            LectureEnrollmentHistoryEntity(lectureId = lectureId, userId = userId, isSuccessful = isSuccessful)
        )
    }
}