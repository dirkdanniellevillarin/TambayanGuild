package com.backand.tambayanguild.entity;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "guild_members")
public class GuildMembersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // one guild can have many members
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "guild_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "owner", "inviteCode"})
    private GuildEntity guild;

    // one user can be a member of many guilds
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "ownedGuilds", "password"})
    private UserEntity user;

    // e.g. LEADER, OFFICER, MEMBER
    @Column(nullable = false)
    private String role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "last_active_date")
    private java.time.LocalDate lastActiveDate;

    @Column(nullable = false)
    private int currentStreak = 0;

    @Column(nullable = false)
    private int bestStreak = 0;

    public GuildMembersEntity() {}

    public GuildMembersEntity(GuildEntity guild, UserEntity user, String role) {
        this.guild = guild;
        this.user = user;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    @PrePersist
    private void setJoinTimeIfMissing() {
        if (joinedAt == null) joinedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public GuildEntity getGuild() { return guild; }
    public UserEntity getUser() { return user; }
    public String getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }

    public void setId(Long id) { this.id = id; }
    public void setGuild(GuildEntity guild) { this.guild = guild; }
    public void setUser(UserEntity user) { this.user = user; }
    public void setRole(String role) { this.role = role; }
    public java.time.LocalDate getLastActiveDate() { return lastActiveDate; }
    public void setLastActiveDate(java.time.LocalDate lastActiveDate) { this.lastActiveDate = lastActiveDate; }
    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }
    public int getBestStreak() { return bestStreak; }
    public void setBestStreak(int bestStreak) { this.bestStreak = bestStreak; }
}
