package kr.com.hhp.lectureapiserver.lecture.infra.jpa

import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureJpaRepository: JpaRepository<LectureEntity, Long>, LectureRepository {
    override fun findByLectureId(lectureId: Long) : LectureEntity?
    override fun findAllByOrderByLectureIdDesc(pageable: Pageable) : Page<LectureEntity>
    override fun save(lectureEntity: LectureEntity): LectureEntity
}