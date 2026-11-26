package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
    @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
public class UserEntity {

    // auto increment rani guys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username
    @Column(nullable = false, unique = true)
    private String username;

    // email add
    @Column(nullable = false, unique = true)
    private String email;

    //pass
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Accept in requests, omit from responses.
    private String password;

    //datecreation
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private long xp = 0;

    @Column(nullable = false)
    private long coins = 0;

    @Column(nullable = false)
    private int currentStreak = 0;

    @Column(nullable = false)
    private int bestStreak = 0;

    @Column(length = 500)
    private String avatarUrl;

    //GuildEntity owner
    @JsonManagedReference   // to serialize the child list normally
    @OneToMany(mappedBy = "owner")
    @JsonIgnore // Prevents recursions
    private List<GuildEntity> ownedGuilds = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
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
    public String getUsername() { 
        return username; 
    }

    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }
    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }

    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }

    public List<GuildEntity> getOwnedGuilds() { 
        return ownedGuilds; 
    }

    public long getXp() { return xp; }
    public void setXp(long xp) { this.xp = xp; }
    public long getCoins() { return coins; }
    public void setCoins(long coins) { this.coins = coins; }
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    public int getBestStreak() { return bestStreak; }
    public void setBestStreak(int bestStreak) { this.bestStreak = bestStreak; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
}
