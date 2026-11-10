package com.backand.tambayanguild.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.backand.tambayanguild.entity.DeckEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "flashcards")
public class FlashcardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one deck can have many flashcards
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "guild", "createdBy"})
    private DeckEntity deck;

    // front of the card
    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    // back of the card
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public FlashcardEntity() {}

    public FlashcardEntity(DeckEntity deck, String question, String answer) {
        this.deck = deck;
        this.question = question;
        this.answer = answer;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setCreationTimeIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public DeckEntity getDeck() { return deck; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setDeck(DeckEntity deck) { this.deck = deck; }
    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }
}