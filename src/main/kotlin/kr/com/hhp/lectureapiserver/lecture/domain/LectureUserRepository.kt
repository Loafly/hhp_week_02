package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import org.springframework.stereotype.Repository

@Repository
interface LectureUserRepository {
    fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureUserEntity?
    fun countAllByLectureId(lectureId: Long): Long
    fun save(lectureUserEntity: LectureUserEntity): LectureUserEntity
}