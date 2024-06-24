package kr.com.hhp.lectureapiserver.lecture.infra

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter
import java.time.LocalDateTime

@Getter
@Entity
@Table(name = "Lecture")
class LectureEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    var lectureId: Long,

    @Column(name = "lecture_date_time", nullable = false)
    var lectureDateTime: LocalDateTime,

    @Column(name = "capacity", nullable = false)
    var capacity: Int = 30,

    @Column(name = "enrollment_period_start", nullable = false)
    var enrollmentPeriodStart: LocalDateTime,

    @Column(name = "enrollment_period_end")
    var enrollmentPeriodEnd: LocalDateTime?
)