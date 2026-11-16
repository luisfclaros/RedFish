package com.RedFish.RedFish.dispatches.interfaces.rest;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeliverDispatchRequest(@NotNull LocalDate deliveryDate, @NotBlank @Size(max=100) String receivedBy) {}
