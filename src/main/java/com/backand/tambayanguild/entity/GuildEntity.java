package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "guilds")
public class GuildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pwede rah maka create ug many guilds ang isa UserEntity pero ang usa ka GuildEntity kay naay usa ra ka owner. Mao nga many to one ni siya.
    @JsonBackReference  // to skip serializing the parent reference on the way back down, stopping the infinite loop
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    //GuildEntity name
    @Column(nullable = false)
    private String name;

    //GuildEntity description(optional)
    @Column(columnDefinition = "TEXT")
    private String description;

    //GuildEntity creation date
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public GuildEntity() {}

    public GuildEntity(UserEntity owner, String name, String description) {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setCreationTimeIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public UserEntity getOwner() { 
        return owner; 
    }

    public void setOwner(UserEntity owner) { 
        this.owner = owner; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getDescription() { 
        return description; 
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
}
