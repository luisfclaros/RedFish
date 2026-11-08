package com.RedFish.RedFish.production.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record FeedingRecordRequest(@NotNull Long pondId, @NotNull Long productId, @NotNull LocalDate date,
        @NotBlank @Size(max=100) String foodType, @NotNull @Positive BigDecimal quantity,
        @NotNull Long registeredBy, @Size(max=500) String observations) {}
