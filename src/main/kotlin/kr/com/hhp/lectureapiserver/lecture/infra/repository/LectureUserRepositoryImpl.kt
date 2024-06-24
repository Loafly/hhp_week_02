package kr.com.hhp.lectureapiserver.lecture.infra.repository

import kr.com.hhp.lectureapiserver.lecture.infra.LectureUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureUserRepositoryImpl: JpaRepository<LectureUserEntity, Long>, LectureUserRepository {

    override fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureUserEntity?
}