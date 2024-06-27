package kr.com.hhp.lectureapiserver.common

import kr.com.hhp.lectureapiserver.lecture.application.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.application.exception.DuplicateApplicationException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureDuplicateException
import kr.com.hhp.lectureapiserver.lecture.application.exception.LectureNotFoundException
import kr.com.hhp.lectureapiserver.user.application.exception.UserDuplicateException
import kr.com.hhp.lectureapiserver.user.application.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

data class ErrorResponse(val message: String?)

@RestControllerAdvice
class ApiControllerAdvice : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [
        CapacityExceededException::class,
        DuplicateApplicationException::class,
        LectureDuplicateException::class,
        UserDuplicateException::class,
    ])
    fun handleCustomBadRequestExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e.message)

        return ResponseEntity(ErrorResponse(e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [
        LectureNotFoundException::class,
        UserNotFoundException::class
    ])
    fun handleCustomNotFoundExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e.message)

        return ResponseEntity(ErrorResponse(e.message), HttpStatus.NOT_FOUND)
    }
}