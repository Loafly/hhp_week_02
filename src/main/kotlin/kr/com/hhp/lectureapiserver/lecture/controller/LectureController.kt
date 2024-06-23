package kr.com.hhp.lectureapiserver.lecture.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lectures")
class LectureController {

    @GetMapping("/application/{userId}")
    fun lecture(
        @PathVariable userId: Long,
    ): Boolean {
        return false;
    }
}