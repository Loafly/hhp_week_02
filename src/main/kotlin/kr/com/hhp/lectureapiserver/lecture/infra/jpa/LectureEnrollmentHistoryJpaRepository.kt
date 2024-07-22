package kr.com.hhp.lectureapiserver.lecture.infra.jpa

import kr.com.hhp.lectureapiserver.lecture.domain.LectureEnrollmentHistoryRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureEnrollmentHistoryJpaRepository: JpaRepository<LectureEnrollmentHistoryEntity, Long>,
    LectureEnrollmentHistoryRepository {
    override fun save(lectureEnrollmentHistoryEntity: LectureEnrollmentHistoryEntity): LectureEnrollmentHistoryEntity
}