package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_username", columnNames = "username"))
public class UserEntity {

    // auto increment rani guys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username
    @Column(nullable = false, unique = true)
    private String username;

    // email add
    @Column(nullable = false)
    private String email;

    //pass
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Accept in requests, omit from responses.
    private String password;

    //datecreation
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //GuildEntity owner
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
}
