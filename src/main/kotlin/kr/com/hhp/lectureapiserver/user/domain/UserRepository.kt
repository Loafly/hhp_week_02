package kr.com.hhp.lectureapiserver.user.domain

import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {
    fun save(userEntity: UserEntity): UserEntity
    fun findByUserId(userId: Long): UserEntity?
}