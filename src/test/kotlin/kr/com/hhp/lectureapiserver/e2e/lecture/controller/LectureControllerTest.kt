package kr.com.hhp.lectureapiserver.e2e.lecture.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
    @DisplayName("특강 신청 여부 조회 API")
    inner class LectureApplicationGetTest {

        @Test
        fun `200 OK (특강 신청이 되어있지 않은 경우)`() {
            // given
            val userId = 1L;
            val lectureId = 1L;

            // when then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/lectures/application/$userId")
                    .param("lectureId", lectureId.toString())
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.lectureId").value(lectureId))
                .andExpect(jsonPath("$.isSuccessful").value(false))
        }
    }

    @Nested
    @DisplayName("특강 신청 API")
    inner class LectureApplyPostTest {

        @Test
        fun `200 OK`() {
            // given
            val userId = 1L;
            val lectureId = 1L;

            val requestBody = objectMapper.writeValueAsString(mapOf("userId" to userId, "lectureId" to lectureId))

            // when then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/lectures/apply")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody)
            )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.lectureId").value(lectureId))
        }
    }
}