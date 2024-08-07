package kr.com.hhp.lectureapiserver.unit.user.application

import kr.com.hhp.lectureapiserver.user.application.UserService
import kr.com.hhp.lectureapiserver.user.application.exception.UserDuplicateException
import kr.com.hhp.lectureapiserver.user.application.exception.UserNotFoundException
import kr.com.hhp.lectureapiserver.user.domain.UserRepository
import kr.com.hhp.lectureapiserver.user.infra.UserEntity
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.given
import org.mockito.kotlin.then

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService

    @Nested
    @DisplayName("유저 조회")
    inner class GetUserTest {
        @Test
        fun `성공 (유저가 존재하는 경우)`() {
            //given
            val userId = 1L
            val expectedUser = UserEntity(userId)
            given(userRepository.findByUserId(userId)).willReturn(expectedUser)

            //when
            val user = userService.getByUserId(userId)

            //then
            then(userRepository).should().findByUserId(userId)
            assertEquals(expectedUser, user)
        }

        @Test
        fun `실패 (유저가 존재하지 않는 경우)`() {
            //given
            val userId = 1L
            given(userRepository.findByUserId(userId)).willReturn(null)

            //when
            val exception = assertThrows<UserNotFoundException> {
                userService.getByUserId(userId)
            }

            //then
            then(userRepository).should().findByUserId(userId)
            assertEquals("User를 찾을 수 없습니다. userId : $userId", exception.message)
        }
    }

    @Nested
    @DisplayName("유저 생성")
    inner class SaveUserTest {
        @Test
        fun `성공 (userId가 생성된게 없는 경우)`() {
            //given
            val userId = 1L
            val expectedUser = UserEntity(userId)

            given(userRepository.findByUserId(userId)).willReturn(null)

            given(userRepository.save(any())).willReturn(expectedUser)

            //when
            val user = userService.save(userId)

            //then
            then(userRepository).should().save(any())
            assertEquals(expectedUser, user)
        }

        @Test
        fun `실패 (유저가 존재하지 않는 경우)`() {
            //given
            val userId = 1L
            val expectedUser = UserEntity(userId)
            given(userRepository.findByUserId(userId)).willReturn(expectedUser)

            //when
            val exception = assertThrows<UserDuplicateException> {
                userService.save(userId)
            }

            //then
            then(userRepository).should().findByUserId(userId)
            assertEquals("동일한Id를 가진 User가 존재합니다. userId : $userId", exception.message)
        }
    }

}