package com.RedFish.RedFish.inventory.interfaces.rest;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InventoryMovementRequest(@NotNull Long productId, @NotNull @Positive BigDecimal quantity,
        @NotNull Long registeredBy, String observations) {}
