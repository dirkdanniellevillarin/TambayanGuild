package com.backand.tambayanguild.dto;

import java.time.LocalDateTime;

public record AuthResponse(Long id, String username, String email, LocalDateTime createdAt) {}
