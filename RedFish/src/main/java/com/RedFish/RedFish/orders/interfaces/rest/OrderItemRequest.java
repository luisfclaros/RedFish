package com.RedFish.RedFish.orders.interfaces.rest;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OrderItemRequest(@NotNull Long productId, @NotBlank @Size(max=100) String productName,
        @NotNull @Positive BigDecimal quantity, @NotNull @Positive BigDecimal unitPrice) {}
