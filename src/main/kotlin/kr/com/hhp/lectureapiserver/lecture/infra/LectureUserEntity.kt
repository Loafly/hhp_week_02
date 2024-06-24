package kr.com.hhp.lectureapiserver.lecture.infra

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import lombok.Getter

@Getter
@Entity
@Table(name = "LectureUser")
class LectureUserEntity (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_user_id")
    var lectureUserId: Long?,

    @Column(name = "lecture_id")
    val lectureId: Long,

    @Column(name = "user_id")
    val userId: Long
) {
    // 생성자
    constructor(lectureId: Long, userId: Long): this(
        lectureUserId = null, lectureId = lectureId, userId = userId
    )
}

