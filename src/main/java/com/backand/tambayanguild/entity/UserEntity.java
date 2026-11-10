package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users") // reserve word nani sya di nani pwede gamiton
public class UserEntity {

    // auto increment rani guys
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username
    @Column(nullable = false)
    private String username;

    // email add
    @Column(nullable = false)
    private String email;

    //pass
    @Column(nullable = false)
    private String password;

    //datecreation
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    //GuildEntity owner
    @JsonManagedReference   // to serialize the child list normally
    @OneToMany(mappedBy = "owner")
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
