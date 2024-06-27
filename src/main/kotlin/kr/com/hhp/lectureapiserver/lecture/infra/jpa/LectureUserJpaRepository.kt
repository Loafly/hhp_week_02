package kr.com.hhp.lectureapiserver.lecture.infra.jpa

import kr.com.hhp.lectureapiserver.lecture.domain.LectureUserRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureUserJpaRepository: JpaRepository<LectureUserEntity, Long>, LectureUserRepository {

    override fun findByUserIdAndLectureId(userId: Long, lectureId: Long): LectureUserEntity?
    override fun countAllByLectureId(lectureId: Long): Long
    override fun save(lectureUserEntity: LectureUserEntity): LectureUserEntity
}