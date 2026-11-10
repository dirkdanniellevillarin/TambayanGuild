package com.backand.tambayanguild.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "decks")
public class DeckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one guild can have many decks
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guild_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "owner"})
    private GuildEntity guild;

    // the member who created the deck
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ownedGuilds", "password"})
    private UserEntity createdBy;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;


    private String subject;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public DeckEntity() {}

    public DeckEntity(GuildEntity guild, UserEntity createdBy, String title, String description, String subject) {
        this.guild = guild;
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.subject = subject;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setCreationTimeIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public GuildEntity getGuild() { return guild; }
    public UserEntity getCreatedBy() { return createdBy; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSubject() { return subject; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setGuild(GuildEntity guild) { this.guild = guild; }
    public void setCreatedBy(UserEntity createdBy) { this.createdBy = createdBy; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setSubject(String subject) { this.subject = subject; }
}