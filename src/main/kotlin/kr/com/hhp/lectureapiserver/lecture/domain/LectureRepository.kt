package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface LectureRepository {
    fun findByLectureId(lectureId: Long) : LectureEntity?
    fun findAllByOrderByLectureIdDesc(pageable: Pageable) : Page<LectureEntity>
    fun save(lectureEntity: LectureEntity): LectureEntity
}