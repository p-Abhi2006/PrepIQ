
package com.prepiq.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    // No-argument constructor required by JPA
    // 1. No-argument constructor required by JPA
    public Question() {
    }

    // 2. Constructor with question and answer
    public Question(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // 3. Constructor with question, answer, and topic
    public Question(String question, String answer, Topic topic) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }
}
