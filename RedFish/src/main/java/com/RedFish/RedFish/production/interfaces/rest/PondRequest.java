package com.RedFish.RedFish.production.interfaces.rest;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PondRequest(@NotBlank @Size(max=30) String code, @NotBlank @Size(max=100) String name,
        @NotBlank @Size(max=150) String location, @NotNull @Positive BigDecimal capacity) {}
