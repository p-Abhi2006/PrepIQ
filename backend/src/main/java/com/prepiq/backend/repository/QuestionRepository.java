
package com.prepiq.backend.repository;

import com.prepiq.backend.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByTopicId(Long topicId);

}
