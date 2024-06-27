package kr.com.hhp.lectureapiserver.lecture.application

import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
import kr.com.hhp.lectureapiserver.lecture.application.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.application.exception.DuplicateApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureDuplicateException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureNotFoundException
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import kr.com.hhp.lectureapiserver.user.application.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureService(
    private val lectureUserService: LectureUserService,
    private val lectureRepository: LectureRepository,
    private val lectureEnrollmentHistoryService: LectureEnrollmentHistoryService,
    private val userService: UserService
): LectureUseCase {

    override fun isLectureEnrolled(userId: Long, lectureId: Long): Boolean {
        val lectureUser = lectureUserService.getNullAbleLectureUser(userId = userId, lectureId = lectureId)
        return lectureUser != null;
    }

    @Transactional
    override fun apply(userId: Long, lectureId: Long) {
        kotlin.runCatching {
            verifyApplyPreconditions(userId = userId, lectureId = lectureId)
        }.onSuccess {
            lectureUserService.save(userId = userId, lectureId = lectureId)
            lectureEnrollmentHistoryService.save(
                userId = userId,
                lectureId = lectureId,
                isSuccessful = true
            )
        }.onFailure { exception ->
            lectureEnrollmentHistoryService.save(
                userId = userId,
                lectureId = lectureId,
                isSuccessful = false
            )

            throw exception
        }
    }

    private fun verifyApplyPreconditions(userId: Long, lectureId: Long) {
        val lecture = getByLectureIdWithXLock(lectureId)
        userService.getByUserId(userId)

        val lectureUser = lectureUserService.getNullAbleLectureUser(userId = userId, lectureId = lectureId)

        if (lectureUser != null) {
            throw DuplicateApplicationException("동일한 특강에 신청완료된 내역이 있습니다. userId : $userId, lectureId : $lectureId")
        }


        val currentStudentCount = lectureUserService.countAllByLectureId(lectureId)

        // 정원 초과
        if (lecture.capacity <= currentStudentCount) {
            throw CapacityExceededException("정원을 초과하였습니다.")
        }
    }

    @Transactional(readOnly = true)
    fun getByLectureId(lectureId: Long): LectureEntity {
        return lectureRepository.findByLectureId(lectureId)
            ?: throw LectureNotFoundException("Lecture를 찾을 수 없습니다. lectureId : $lectureId")
    }

    @Transactional(readOnly = true)
    fun getByLectureIdWithXLock(lectureId: Long): LectureEntity {
        return lectureRepository.findByLectureIdWithXLock(lectureId)
            ?: throw LectureNotFoundException("Lecture를 찾을 수 없습니다. lectureId : $lectureId")
    }

    @Transactional(readOnly = true)
    override fun getAllByOrderByLectureIdDesc(pageable: Pageable): Page<LectureEntity> {
        return lectureRepository.findAllByOrderByLectureIdDesc(pageable)
    }

    @Transactional
    fun save(lectureId: Long): LectureEntity {
        kotlin.runCatching {
            getByLectureId(lectureId)
        }.onSuccess {
            throw LectureDuplicateException("동일한Id를 가진 Lecture가 존재합니다. lectureId : $lectureId")
        }

        return lectureRepository.save(LectureEntity(lectureId))
    }
}