package kr.com.hhp.lectureapiserver.lecture.infra.jpa

import jakarta.persistence.LockModeType
import kr.com.hhp.lectureapiserver.lecture.domain.LectureRepository
import kr.com.hhp.lectureapiserver.lecture.infra.entity.LectureEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface LectureJpaRepository: JpaRepository<LectureEntity, Long>, LectureRepository {
    override fun findByLectureId(lectureId: Long) : LectureEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE) //LectureId가 동일한 데이터만 비관적 Lock
    @Query("SELECT lecture FROM LectureEntity as lecture WHERE lecture.lectureId = :lectureId")
    override fun findByLectureIdWithXLock(lectureId: Long) : LectureEntity?
    override fun findAllByOrderByLectureIdDesc(pageable: Pageable) : Page<LectureEntity>
    override fun save(lectureEntity: LectureEntity): LectureEntity
}