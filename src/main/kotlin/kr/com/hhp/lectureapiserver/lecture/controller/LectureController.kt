package kr.com.hhp.lectureapiserver.lecture.controller

import kr.com.hhp.lectureapiserver.lecture.application.LectureService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lectures")
class LectureController(private val lectureService: LectureService) {

    @GetMapping("/application/{userId}")
    fun lecture(
        @PathVariable userId: Long,
        @RequestParam lectureId: Long,
    ): LectureDto.LectureEnrolledResponse {

        val lectureEnrolled = lectureService.isLectureEnrolled(userId, lectureId)

        return LectureDto.LectureEnrolledResponse(
            lectureId = lectureId,
            userId = userId,
            isSuccessful = lectureEnrolled
        )
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: LectureDto.PostRequest): LectureDto {
        val savedLecture = lectureService.save(capacity = request.capacity)

        return LectureDto.of(savedLecture)
    }


    @PostMapping("/apply")
    @ResponseStatus(HttpStatus.CREATED)
    fun applyLecture(@RequestBody request: LectureDto.ApplyLectureRequest): LectureDto.ApplyLectureResponse {

        val userId = request.userId
        val lectureId = request.lectureId

        lectureService.apply(userId = userId, lectureId = lectureId)

        return LectureDto.ApplyLectureResponse(userId = userId, lectureId = lectureId)
    }

    @GetMapping
    fun getAll(pageable: Pageable) : Page<LectureDto> {
        return lectureService.getAllByOrderByLectureIdDesc(pageable)
            .map { lectureEntity ->
                LectureDto.of(lectureEntity)
            }
    }
}