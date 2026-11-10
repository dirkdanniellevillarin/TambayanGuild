package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guilds")
public class Guild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pwede rah maka create ug many guilds ang isa user pero ang usa ka guild kay naay usa ra ka owner. Mao nga many to one ni siya.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    //guild name
    @Column(nullable = false)
    private String name;

    //guild description(optional)
    @Column(columnDefinition = "TEXT")
    private String description;

    //guild creation date
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Guild() {}

    public Guild(User owner, String name, String description) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setCreationTimeIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
