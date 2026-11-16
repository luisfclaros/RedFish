package com.RedFish.RedFish.vehicles.interfaces.rest;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank @Size(max = 10) String plate,
        @NotBlank @Size(max = 60) String brand,
        @NotBlank @Size(max = 60) String model,
        @NotNull @PositiveOrZero BigDecimal loadCapacity,
        String observations) {
}
