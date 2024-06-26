package kr.com.hhp.lectureapiserver.lecture.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lecture")
class LectureEntity (
    @Id
    @Column(name = "lecture_id")
    var lectureId: Long,

    @Column(name = "capacity", nullable = false)
    var capacity: Int = 30,
)