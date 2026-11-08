package com.RedFish.RedFish.security.application.service;

import java.time.LocalDateTime;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.security.application.port.UserRepositoryPort;
import com.RedFish.RedFish.security.domain.model.Role;
import com.RedFish.RedFish.security.domain.model.User;

@Service
public class RegisterUserService {
    private final UserRepositoryPort users;
    private final PasswordEncoder encoder;
    public RegisterUserService(UserRepositoryPort users, PasswordEncoder encoder) {
        this.users=users; this.encoder=encoder;
    }
    @Transactional
    public User register(String name, String username, String rawPassword, String roleName) {
        if (rawPassword == null || rawPassword.length() < 10)
            throw new IllegalArgumentException("password must contain at least 10 characters");
        if (users.existsByUsername(username))
            throw new IllegalArgumentException("username already exists");
        String normalizedRole=roleName == null ? "OPERATOR" : roleName.trim().toUpperCase();
        if (!normalizedRole.equals("ADMIN") && !normalizedRole.equals("OPERATOR"))
            throw new IllegalArgumentException("role must be ADMIN or OPERATOR");
        return users.save(new User(null, name, username, encoder.encode(rawPassword),
                new Role(null, normalizedRole, normalizedRole), true, LocalDateTime.now()));
    }
}
