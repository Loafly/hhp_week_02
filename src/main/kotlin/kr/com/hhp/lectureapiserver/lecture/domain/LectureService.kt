package kr.com.hhp.lectureapiserver.lecture.domain

import org.springframework.stereotype.Service

@Service
class LectureService(private val lectureUserService: LectureUserService) {

    fun isLectureEnrolled(userId: Long, lectureId: Long): Boolean {
        val lectureUser = lectureUserService.getNullAbleLectureUser(userId, lectureId)
        return lectureUser != null;
    }
}