package kr.com.hhp.lectureapiserver.e2e.lecture.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Nested
    @DisplayName("특강 신청 여부 조회")
    inner class LectureApplicationGetTest {

        @Test
        fun `200 OK`() {
            // given
            val id = 1L;

            // when then
            mockMvc.perform(MockMvcRequestBuilders.get("/lectures/application/$id"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$").value(false))
        }
    }
}