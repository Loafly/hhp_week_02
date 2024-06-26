package kr.com.hhp.lectureapiserver.lecture.application

import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
import kr.com.hhp.lectureapiserver.lecture.application.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.application.exception.DuplicateApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.EarlyApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LateApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureNotFoundException
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class LectureService(
    private val lectureUserService: LectureUserService,
    private val lectureRepository: LectureRepository,
    private val lectureEnrollmentHistoryService: LectureEnrollmentHistoryService
) {

    fun isLectureEnrolled(userId: Long, lectureId: Long): Boolean {
        val lectureUser = lectureUserService.getNullAbleLectureUser(userId = userId, lectureId = lectureId)
        return lectureUser != null;
    }

    fun apply(userId: Long, lectureId: Long) {
        // TODO : 코드 수정이 필요 해 보이는데 어떻게 처리해야 잘한 코드일까??
        var isEnrollmentSuccessful = true
        try {
            verifyApplyPreconditions(userId = userId, lectureId = lectureId)
            lectureUserService.save(userId = userId, lectureId = lectureId)
        } catch (e: RuntimeException) {
            isEnrollmentSuccessful = false
            throw e
        } finally {
            lectureEnrollmentHistoryService.save(
                userId = userId,
                lectureId = lectureId,
                isSuccessful = isEnrollmentSuccessful
            )
        }
    }

    private fun verifyApplyPreconditions(userId: Long, lectureId: Long) {
        val lectureUser = lectureUserService.getNullAbleLectureUser(userId = userId, lectureId = lectureId)

        if (lectureUser != null) {
            throw DuplicateApplicationException("동일한 특강에 신청완료된 내역이 있습니다. userId : $userId, lectureId : $lectureId")
        }

        val lecture = getByLectureId(lectureId)
        val currentStudentCount = lectureUserService.countAllByLectureId(lectureId)

        // 정원 초과
        if (lecture.capacity <= currentStudentCount) {
            throw CapacityExceededException("정원을 초과하였습니다.")
        }

        if (lecture.enrollmentPeriodStart > LocalDateTime.now()) {
            throw EarlyApplicationException("특강 신청 기간이 아닙니다.")
        }

        lecture.enrollmentPeriodEnd?.let {
            if (it < LocalDateTime.now()) {
                throw LateApplicationException("특강 신청 기간이 아닙니다.")
            }
        }
    }

    @Transactional(readOnly = true)
    fun getByLectureId(lectureId: Long): LectureEntity {
        return lectureRepository.findByLectureId(lectureId)
            ?: throw LectureNotFoundException("lecture가 존재하지 않습니다. lectureId : $lectureId")
    }

    @Transactional(readOnly = true)
    fun getAllByOrderByLectureIdDesc(pageable: Pageable): Page<LectureEntity> {
        return lectureRepository.findAllByOrderByLectureIdDesc(pageable)
    }

    @Transactional
    fun save(lectureDateTime: LocalDateTime, capacity: Int, enrollmentPeriodStart: LocalDateTime, enrollmentPeriodEnd: LocalDateTime?): LectureEntity {
        val lectureEntity = LectureEntity(
            lectureDateTime = lectureDateTime,
            capacity = capacity,
            enrollmentPeriodStart = enrollmentPeriodStart,
            enrollmentPeriodEnd = enrollmentPeriodEnd
        )

        return lectureRepository.save(lectureEntity)
    }
}