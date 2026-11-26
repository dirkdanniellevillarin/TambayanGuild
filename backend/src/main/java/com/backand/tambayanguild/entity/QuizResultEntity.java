package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results", uniqueConstraints = @UniqueConstraint(columnNames = {"group_quiz_id", "user_id"}))
public class QuizResultEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "group_quiz_id", nullable = false)
    private GroupQuizEntity groupQuiz;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    @Column(nullable = false) private int score;
    @Column(nullable = false) private int totalQuestions;
    @Column(nullable = false) private LocalDateTime completedAt;

    protected QuizResultEntity() {}
    public QuizResultEntity(GroupQuizEntity groupQuiz, UserEntity user, int score, int totalQuestions) {
        this.groupQuiz = groupQuiz; this.user = user; this.score = score;
        this.totalQuestions = totalQuestions; this.completedAt = LocalDateTime.now();
    }
    @PrePersist private void defaults() { if (completedAt == null) completedAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public GroupQuizEntity getGroupQuiz() { return groupQuiz; }
    public UserEntity getUser() { return user; }
    public int getScore() { return score; }
    public int getTotalQuestions() { return totalQuestions; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
