package com.RedFish.RedFish.dispatches.interfaces.rest;

import java.time.LocalDate;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;

public record DispatchResponse(Long id, Long orderId, Long vehicleId, LocalDate dispatchDate,
        LocalDate deliveryDate, DispatchStatus status, String deliveryAddress, String receivedBy) {
    public static DispatchResponse from(Dispatch d) {
        return new DispatchResponse(d.id(), d.orderId(), d.vehicleId(), d.dispatchDate(),
                d.deliveryDate(), d.status(), d.deliveryAddress(), d.receivedBy());
    }
}
