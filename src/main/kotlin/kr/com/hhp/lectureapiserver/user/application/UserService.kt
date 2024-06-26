package kr.com.hhp.lectureapiserver.user.application

import kr.com.hhp.lectureapiserver.user.application.exception.UserNotFoundException
import kr.com.hhp.lectureapiserver.user.domain.UserRepository
import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getById(userId: Long): UserEntity {

        return userRepository.findByUserId(userId = userId)
            ?: throw UserNotFoundException("user가 존재하지 않습니다. userId = $userId")
    }

}