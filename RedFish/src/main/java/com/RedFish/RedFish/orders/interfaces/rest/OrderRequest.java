package com.RedFish.RedFish.orders.interfaces.rest;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderRequest(@NotNull Long customerId, @NotNull LocalDate date,
        @NotEmpty List<@Valid OrderItemRequest> items, @Size(max=500) String observations) {}
