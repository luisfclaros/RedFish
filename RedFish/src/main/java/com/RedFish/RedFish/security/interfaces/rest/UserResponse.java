package com.RedFish.RedFish.security.interfaces.rest;

import java.time.LocalDateTime;
import com.RedFish.RedFish.security.domain.model.User;

public record UserResponse(Long id, String name, String username, String role, boolean active,
        LocalDateTime createdAt) {
    public static UserResponse from(User u) {
        return new UserResponse(u.id(), u.name(), u.username(), u.role().name(), u.active(), u.createdAt());
    }
}
