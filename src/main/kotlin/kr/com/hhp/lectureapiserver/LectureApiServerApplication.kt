package kr.com.hhp.lectureapiserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LectureApiServerApplication

fun main(args: Array<String>) {
	runApplication<LectureApiServerApplication>(*args)
}
