package kr.com.hhp.lectureapiserver.lecture.application

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface LectureUseCase {
    fun isLectureEnrolled(userId: Long, lectureId: Long): Boolean
    fun apply(userId: Long, lectureId: Long)
    fun getAllByOrderByLectureIdDesc(pageable: Pageable): Page<LectureEntity>
}