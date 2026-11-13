package com.RedFish.RedFish.security.interfaces.rest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotBlank @Size(max=100) String name,
        @NotBlank @Size(max=60) String username,
        @NotBlank @Size(min=10, max=100) String password,
        @NotBlank String role) {}
