
package com.prepiq.backend.service;

import com.prepiq.backend.dto.QuestionResponse;
import com.prepiq.backend.entity.Question;
import com.prepiq.backend.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionResponse> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    public List<QuestionResponse> getQuestionsByTopicId(Long topicId) {
        return questionRepository.findByTopicId(topicId)
                .stream()
                .map(this::toQuestionResponse)
                .toList();
    }

    private QuestionResponse toQuestionResponse(Question question) {
        return new QuestionResponse(
                question.getId(),
                question.getQuestion(),
                question.getAnswer(),
                question.getTopic() != null
                        ? question.getTopic().getId()
                        : null,
                question.getTopic() != null
                        ? question.getTopic().getName()
                        : null
        );
    }
}
