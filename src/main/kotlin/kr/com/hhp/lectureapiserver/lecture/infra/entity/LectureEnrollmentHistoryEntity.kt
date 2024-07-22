package kr.com.hhp.lectureapiserver.lecture.infra.entity

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
@Table(name = "lecture_enrollment_history")
class LectureEnrollmentHistoryEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var lectureEnrollmentHistoryId: Long? = null,

    @Column(name = "lecture_id", nullable = false)
    var lectureId: Long,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    // Status로 관리하는게 더 좋을까....?
    @Column(name = "is_successful", nullable = false)
    var isSuccessful: Boolean,

    @Column(name = "create_at", nullable = false)
    var createAt: LocalDateTime = LocalDateTime.now()

)
