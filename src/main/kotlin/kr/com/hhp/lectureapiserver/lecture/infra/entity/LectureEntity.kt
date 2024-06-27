package kr.com.hhp.lectureapiserver.lecture.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "lecture")
class LectureEntity (
    @Id
    @Column(name = "lecture_id")
    var lectureId: Long,

    @Column(name = "capacity", nullable = false)
    var capacity: Int = 30,

    @OneToMany(mappedBy = "lectureId", fetch = FetchType.LAZY)
    var lectureSchedules: List<LectureScheduleEntity> = mutableListOf()

)