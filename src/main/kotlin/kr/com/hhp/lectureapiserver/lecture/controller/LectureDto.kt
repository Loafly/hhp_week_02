package kr.com.hhp.lectureapiserver.lecture.controller

import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import java.time.LocalDateTime

data class LectureDto (
    val lectureId: Long,
    val lectureDateTime: LocalDateTime,
    val capacity: Int,
    val enrollmentPeriodStart: LocalDateTime,
    val enrollmentPeriodEnd: LocalDateTime?
) {
    companion object {
        fun of (lectureEntity: LectureEntity) : LectureDto {
            return LectureDto(
                lectureId = lectureEntity.lectureId!!,
                lectureDateTime = lectureEntity.lectureDateTime,
                capacity = lectureEntity.capacity,
                enrollmentPeriodStart = lectureEntity.enrollmentPeriodStart,
                enrollmentPeriodEnd = lectureEntity.enrollmentPeriodEnd
            )
        }
    }

    data class PostRequest (
        val lectureDateTime: LocalDateTime,
        val capacity: Int = 30,
        val enrollmentPeriodStart: LocalDateTime,
        val enrollmentPeriodEnd: LocalDateTime?,
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
