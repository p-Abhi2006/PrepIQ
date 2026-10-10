
package com.prepiq.backend.controller;

import com.prepiq.backend.dto.QuestionResponse;
import com.prepiq.backend.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    @Test
    void getAllQuestionsShouldReturnQuestions() throws Exception {
        when(questionService.getAllQuestions()).thenReturn(List.of(
                new QuestionResponse(
                        1L,
                        "What is an array?",
                        "A collection of elements.",
                        1L,
                        "DSA"
                )
        ));

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question").value("What is an array?"))
                .andExpect(jsonPath("$[0].topic").value("DSA"));
    }

    @Test
    void getQuestionsByTopicShouldReturnFilteredQuestions() throws Exception {
        when(questionService.getQuestionsByTopicId(1L)).thenReturn(List.of(
                new QuestionResponse(
                        1L,
                        "What is an array?",
                        "A collection of elements.",
                        1L,
                        "DSA"
                )
        ));

        mockMvc.perform(get("/api/questions/by-topic")
                        .param("topicId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].topicId").value(1))
                .andExpect(jsonPath("$[0].topic").value("DSA"));
    }
}
