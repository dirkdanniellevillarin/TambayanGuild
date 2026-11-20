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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "owner"})
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
}