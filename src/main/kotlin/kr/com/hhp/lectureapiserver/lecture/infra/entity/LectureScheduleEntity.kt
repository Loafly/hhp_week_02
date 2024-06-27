package kr.com.hhp.lectureapiserver.lecture.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "lecture_schedule")
class LectureScheduleEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var lectureScheduleId: Long? = null,

    @Column(name = "lecture_id", nullable = false)
    var lectureId: Long,

    @Column(name = "lecture_date_time", nullable = false)
    var lectureDateTime: LocalDateTime,
)
