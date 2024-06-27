package kr.com.hhp.lectureapiserver.user.application

import kr.com.hhp.lectureapiserver.user.domain.UserRepository
import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getOrInsertById(userId: Long): UserEntity {
        return userRepository.findByUserId(userId = userId)
            ?: userRepository.save(UserEntity(userId = userId))
    }

}