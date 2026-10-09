
package com.prepiq.backend.dto;

public record QuestionResponse(
        Long id,
        String question,
        String answer,
        Long topicId,
        String topic
) {
}
