package com.RedFish.RedFish.inventory.interfaces.rest;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryStockRequest(@NotNull Long productId, @NotNull @PositiveOrZero BigDecimal initialQuantity,
        @NotNull @PositiveOrZero BigDecimal minimumStock) {}
