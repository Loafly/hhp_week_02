package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEnrollmentHistoryEntity
import org.springframework.stereotype.Repository

@Repository
interface LectureEnrollmentHistoryRepository {
    fun save(lectureEnrollmentHistoryEntity: LectureEnrollmentHistoryEntity): LectureEnrollmentHistoryEntity
}