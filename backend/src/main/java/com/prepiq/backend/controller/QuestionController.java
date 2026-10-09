
package com.prepiq.backend.controller;

import com.prepiq.backend.dto.QuestionResponse;
import com.prepiq.backend.service.QuestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<QuestionResponse> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/by-topic")
    public List<QuestionResponse> getQuestionsByTopic(
            @RequestParam Long topicId) {
        return questionService.getQuestionsByTopicId(topicId);
    }
}
