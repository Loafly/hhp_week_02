package kr.com.hhp.lectureapiserver.lecture.domain

import org.springframework.stereotype.Service

@Service
class LectureService {

    fun getByLectureId(lectureId: Long) {

    }

    fun isLectureEnrolled(userId: Long, lectureId: Long): Boolean {
        return false;
    }
}