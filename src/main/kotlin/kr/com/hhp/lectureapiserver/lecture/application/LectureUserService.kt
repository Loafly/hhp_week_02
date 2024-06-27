package kr.com.hhp.lectureapiserver.lecture.application

import kr.com.hhp.lectureapiserver.lecture.domain.LectureUserRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureUserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureUserService(private val lectureUserRepository: LectureUserRepository) {

    @Transactional(readOnly = true)
    fun getNullAbleLectureUser(userId: Long, lectureId: Long): LectureUserEntity? {
        return lectureUserRepository.findByUserIdAndLectureId(userId = userId, lectureId = lectureId)
    }

    @Transactional(readOnly = true)
    fun countAllByLectureId(lectureId: Long): Long {
        return lectureUserRepository.countAllByLectureId(lectureId)
    }

    @Transactional
    fun save(userId: Long, lectureId: Long): LectureUserEntity {
        return lectureUserRepository.save(LectureUserEntity(userId = userId, lectureId = lectureId))
    }
}