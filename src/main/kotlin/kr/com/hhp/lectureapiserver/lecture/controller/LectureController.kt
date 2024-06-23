package kr.com.hhp.lectureapiserver.lecture.controller

import kr.com.hhp.lectureapiserver.lecture.domain.LectureService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lectures")
class LectureController(private val lectureService: LectureService) {

    @GetMapping("/application/{userId}")
    fun lecture(
        @PathVariable userId: Long,
        @RequestParam lectureId: Long,
    ): Boolean {
        return lectureService.isLectureEnrolled(userId, lectureId);
    }
}