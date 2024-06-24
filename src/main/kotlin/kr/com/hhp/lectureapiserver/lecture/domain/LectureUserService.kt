package kr.com.hhp.lectureapiserver.lecture.domain

import kr.com.hhp.lectureapiserver.lecture.infra.LectureUserEntity
import kr.com.hhp.lectureapiserver.lecture.infra.repository.LectureUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureUserService(private val lectureUserRepository: LectureUserRepository) {

    @Transactional(readOnly = true)
    fun getNullAbleLectureUser(userId: Long, lectureId: Long): LectureUserEntity? {
        return lectureUserRepository.findByUserIdAndLectureId(lectureId, userId)
    }

}