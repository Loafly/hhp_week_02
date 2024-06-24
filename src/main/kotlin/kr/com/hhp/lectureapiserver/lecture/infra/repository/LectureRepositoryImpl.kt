package kr.com.hhp.lectureapiserver.lecture.infra.repository

import kr.com.hhp.lectureapiserver.lecture.infra.LectureEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LectureRepositoryImpl: JpaRepository<LectureEntity, Int>, LectureRepository {
}