package kr.com.hhp.lectureapiserver.user.infra

import kr.com.hhp.lectureapiserver.user.domain.UserRepository
import org.springframework.data.jpa.repository.JpaRepository

interface UserJpaRepository: JpaRepository<UserEntity, Long>, UserRepository {
    override fun save(userEntity: UserEntity): UserEntity
    override fun findByUserId(userId: Long): UserEntity?
}