package com.backand.tambayanguild.service;

import com.backand.tambayanguild.dto.AuthRequest;
import com.backand.tambayanguild.dto.AuthResponse;
import com.backand.tambayanguild.dto.LoginRequest;
import com.backand.tambayanguild.entity.UserEntity;
import com.backand.tambayanguild.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final UserRepository users;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository users) {
        this.users = users;
    }

    public AuthResponse register(AuthRequest request) {
        if (request.displayName() == null || request.displayName().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Display name, email, and a password of at least 8 characters are required");
        }

        String email = request.email().trim().toLowerCase();
        if(!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Enter a valid email address");
        if (users.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }

        String username = request.displayName().trim();
        if (users.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "That display name is already taken");
        }

        UserEntity user = new UserEntity(username, email,
                passwordEncoder.encode(request.password()));
        UserEntity saved=users.save(user);
        return toResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        if (request.email() == null || request.password() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        UserEntity user = users.findByEmailIgnoreCase(request.email().trim())
                .filter(candidate -> passwordEncoder.matches(request.password(), candidate.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        return toResponse(user);
    }

    private AuthResponse toResponse(UserEntity user) {
        return new AuthResponse(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
