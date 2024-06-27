package kr.com.hhp.lectureapiserver.lecture.controller

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureScheduleEntity

data class LectureDto (
    val lectureId: Long,
    val capacity: Int,
    val lectureSchedules: List<LectureScheduleEntity>
) {
    companion object {
        fun of (lectureEntity: LectureEntity) : LectureDto {
            return LectureDto(
                lectureId = lectureEntity.lectureId!!,
                capacity = lectureEntity.capacity,
                lectureSchedules = lectureEntity.lectureSchedules
            )
        }
    }

    data class PostRequest (
        val capacity: Int = 30
    )

    data class LectureEnrolledResponse(
        val userId: Long,
        val lectureId: Long,
        val isSuccessful: Boolean
    )

    data class ApplyLectureRequest(
        val userId: Long,
        val lectureId: Long
    )

    data class ApplyLectureResponse(
        val userId: Long,
        val lectureId: Long,
    )
}
