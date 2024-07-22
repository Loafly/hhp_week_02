package kr.com.hhp.lectureapiserver.user.application

import kr.com.hhp.lectureapiserver.user.application.exception.UserDuplicateException
import kr.com.hhp.lectureapiserver.user.application.exception.UserNotFoundException
import kr.com.hhp.lectureapiserver.user.domain.UserRepository
import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getByUserId(userId: Long): UserEntity {
        return userRepository.findByUserId(userId = userId)
            ?: throw UserNotFoundException("User를 찾을 수 없습니다. userId : $userId")
    }

    @Transactional
    fun save(userId: Long): UserEntity {
        kotlin.runCatching {
            getByUserId(userId)
        }.onSuccess {
            throw UserDuplicateException("동일한Id를 가진 User가 존재합니다. userId : $userId")
        }

        return userRepository.save(UserEntity(userId))
    }

}