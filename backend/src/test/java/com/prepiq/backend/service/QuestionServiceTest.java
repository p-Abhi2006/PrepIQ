
package com.prepiq.backend.service;

import com.prepiq.backend.dto.QuestionResponse;
import com.prepiq.backend.entity.Question;
import com.prepiq.backend.entity.Topic;
import com.prepiq.backend.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question question;

    @BeforeEach
    void setUp() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("DSA");

        question = new Question(
                "What is an array?",
                "A collection of elements stored in contiguous memory locations.",
                topic
        );
    }

    @Test
    void getAllQuestionsShouldReturnMappedQuestions() {
        when(questionRepository.findAll()).thenReturn(List.of(question));

        List<QuestionResponse> responses = questionService.getAllQuestions();

        assertEquals(1, responses.size());
        assertEquals("What is an array?", responses.get(0).question());
        assertEquals("DSA", responses.get(0).topic());
        assertEquals(1L, responses.get(0).topicId());

        verify(questionRepository).findAll();
    }

    @Test
    void getQuestionsByTopicIdShouldReturnMappedQuestions() {
        when(questionRepository.findByTopicId(1L))
                .thenReturn(List.of(question));

        List<QuestionResponse> responses =
                questionService.getQuestionsByTopicId(1L);

        assertEquals(1, responses.size());
        assertEquals("What is an array?", responses.get(0).question());
        assertEquals("DSA", responses.get(0).topic());

        verify(questionRepository).findByTopicId(1L);
    }
}
