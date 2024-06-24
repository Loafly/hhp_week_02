package kr.com.hhp.lectureapiserver.lecture.infra.repository

import kr.com.hhp.lectureapiserver.lecture.infra.LectureUserEntity
import org.springframework.stereotype.Repository

@Repository
interface LectureUserRepository {
    fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureUserEntity?
}