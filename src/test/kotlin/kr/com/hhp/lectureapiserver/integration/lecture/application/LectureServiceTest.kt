package kr.com.hhp.lectureapiserver.integration.lecture.application

import kr.com.hhp.lectureapiserver.lecture.application.LectureService
import kr.com.hhp.lectureapiserver.lecture.application.LectureUserService
import kr.com.hhp.lectureapiserver.user.application.UserService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.test.assertEquals

@SpringBootTest
class LectureServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var lectureService: LectureService

    @Autowired
    private lateinit var lectureUserService: LectureUserService

    @Test
    fun `특강 신청 비관적 Lock 테스트`() {
        // given
        val lectureId = 1L
        val numberOfThreads = 50
        val maxConcurrentThreads = 10

        val executor = Executors.newCachedThreadPool()
        val latch = CountDownLatch(numberOfThreads)
        val semaphore = Semaphore(maxConcurrentThreads)
        val lectureEntity = lectureService.save(lectureId)
        val capacity = lectureEntity.capacity

        for (i in 1 .. numberOfThreads) {
            userService.save(i.toLong())
        }

        //when
        for (i in 1 ..  numberOfThreads) {
            executor.submit {
                semaphore.acquire()
                try {
                    lectureService.apply(userId = i.toLong(), lectureId = lectureId)
                } finally {
                    semaphore.release()
                    latch.countDown()
                }
            }
        }
        latch.await()
        executor.shutdown()

        val countAllByLectureId = lectureUserService.countAllByLectureId(lectureId)

        assertEquals(capacity.toLong(), countAllByLectureId)
    }
}