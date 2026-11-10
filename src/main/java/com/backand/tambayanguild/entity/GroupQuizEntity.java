package com.backand.tambayanguild.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "group_quizzes")
public class GroupQuizEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one guild can host many group quizzes
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guild_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "owner"})
    private GuildEntity guild;

    // the member who hosts the quiz
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ownedGuilds", "password"})
    private UserEntity createdBy;

    // deck the questions come from (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "guild", "createdBy"})
    private DeckEntity deck;

    @Column(nullable = false)
    private String title;

    // WAITING, ACTIVE, FINISHED
    @Column(nullable = false)
    private String status;

    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public GroupQuizEntity() {}

    public GroupQuizEntity(GuildEntity guild, UserEntity createdBy, DeckEntity deck, String title, Integer timeLimitSeconds) {
        this.guild = guild;
        this.createdBy = createdBy;
        this.deck = deck;
        this.title = title;
        this.status = "WAITING";
        this.timeLimitSeconds = timeLimitSeconds;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setDefaultsIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (status == null) status = "WAITING";
    }

    public Long getId() { return id; }
    public GuildEntity getGuild() { return guild; }
    public UserEntity getCreatedBy() { return createdBy; }
    public DeckEntity getDeck() { return deck; }
    public String getTitle() { return title; }
    public String getStatus() { return status; }
    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setGuild(GuildEntity guild) { this.guild = guild; }
    public void setCreatedBy(UserEntity createdBy) { this.createdBy = createdBy; }
    public void setDeck(DeckEntity deck) { this.deck = deck; }
    public void setTitle(String title) { this.title = title; }
    public void setStatus(String status) { this.status = status; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
}