package com.RedFish.RedFish.security.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.security.application.port.UserRepositoryPort;
import com.RedFish.RedFish.security.application.service.RegisterUserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final RegisterUserService register;
    private final UserRepositoryPort users;
    public UserController(RegisterUserService register, UserRepositoryPort users) {
        this.register=register; this.users=users;
    }
    @PostMapping("/users")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        var user=register.register(request.name(), request.username(), request.password(), request.role());
        return ResponseEntity.status(201).body(UserResponse.from(user));
    }
    public record ChangePasswordRequest(String password) {}

    @PutMapping("/users/{username}/password")
    public ResponseEntity<UserResponse> changePassword(
            @PathVariable String username, @RequestBody ChangePasswordRequest request) {
        var user = register.changePassword(username, request.password());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/auth/me")
    public UserResponse me(Authentication authentication) {
        var user=users.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        return UserResponse.from(user);
    }
}
