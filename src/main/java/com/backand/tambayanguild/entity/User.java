package com.backand.tambayanguild.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // reserve word nani sya di nani pwede gamiton
public class User {

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

    //guild owner
    @OneToMany(mappedBy = "owner")
    private List<Guild> ownedGuilds = new ArrayList<>();

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void setCreationTimeIfMissing() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Guild> getOwnedGuilds() { return ownedGuilds; }
}
