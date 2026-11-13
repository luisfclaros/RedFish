package com.RedFish.RedFish.dispatches.interfaces.rest;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ScheduleDispatchRequest(@NotNull Long orderId, @NotNull Long vehicleId,
        @NotNull LocalDate dispatchDate, @NotBlank @Size(max=250) String deliveryAddress) {}
