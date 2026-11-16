package com.RedFish.RedFish.security.application.service;

import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.security.application.port.UserRepositoryPort;
import com.RedFish.RedFish.security.domain.model.Role;
import com.RedFish.RedFish.security.domain.model.User;

@Service
public class RegisterUserService {
    private static final Set<String> ALLOWED_ROLES =
            Set.of("ADMIN", "PRODUCTION", "OPERATIONS", "COMMERCIAL", "OPERATOR");
    private final UserRepositoryPort users;
    private final PasswordEncoder encoder;

    public RegisterUserService(UserRepositoryPort users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public User changePassword(String username, String rawPassword) {
        if (rawPassword == null || rawPassword.length() < 10)
            throw new IllegalArgumentException("password must contain at least 10 characters");
        User existing = users.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        User updated = new User(existing.id(), existing.name(), existing.username(),
                encoder.encode(rawPassword), existing.role(), existing.active(), existing.createdAt());
        return users.save(updated);
    }

    @Transactional
    public User register(String name, String username, String rawPassword, String roleName) {
        if (rawPassword == null || rawPassword.length() < 10)
            throw new IllegalArgumentException("password must contain at least 10 characters");
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("username is required");
        if (users.existsByUsername(username))
            throw new IllegalArgumentException("username already exists");
        String normalizedRole = roleName == null ? "OPERATOR" : roleName.trim().toUpperCase();
        if (!ALLOWED_ROLES.contains(normalizedRole))
            throw new IllegalArgumentException("role must be ADMIN, PRODUCTION, OPERATIONS or COMMERCIAL");
        return users.save(new User(null, name, username, encoder.encode(rawPassword),
                new Role(null, normalizedRole, normalizedRole), true, LocalDateTime.now()));
    }
}
