package kr.com.hhp.lectureapiserver.lecture.controller

import kr.com.hhp.lectureapiserver.lecture.application.exception.CapacityExceededException
import kr.com.hhp.lectureapiserver.lecture.application.exception.DuplicateApplicationException
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
    ])
    fun handleCommonExceptions(e: Exception): ResponseEntity<ErrorResponse> {
        logger.error(e.message)

        return ResponseEntity(ErrorResponse(e.message), HttpStatus.BAD_REQUEST)
    }
}