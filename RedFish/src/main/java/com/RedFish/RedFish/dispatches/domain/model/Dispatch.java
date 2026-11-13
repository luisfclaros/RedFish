package com.RedFish.RedFish.dispatches.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.LocalDate;

public class Dispatch {

    private final Long id;
    private final Long orderId;
    private final Long vehicleId;
    private final LocalDate dispatchDate;
    private LocalDate deliveryDate;
    private DispatchStatus status;
    private final String deliveryAddress;
    private String receivedBy;

    public Dispatch(Long id, Long orderId, Long vehicleId, LocalDate dispatchDate, String deliveryAddress) {
        this(id, orderId, vehicleId, dispatchDate, null, DispatchStatus.SCHEDULED, deliveryAddress, null);
    }

    private Dispatch(Long id, Long orderId, Long vehicleId, LocalDate dispatchDate, LocalDate deliveryDate,
            DispatchStatus status, String deliveryAddress, String receivedBy) {
        this.id = id;
        this.orderId = requireNonNull(orderId, "order id");
        this.vehicleId = requireNonNull(vehicleId, "vehicle id");
        this.dispatchDate = requireNonNull(dispatchDate, "dispatch date");
        this.deliveryDate = deliveryDate;
        this.status = requireNonNull(status, "dispatch status");
        this.deliveryAddress = requireText(deliveryAddress, "delivery address");
        this.receivedBy = receivedBy;
    }

    public static Dispatch restore(Long id, Long orderId, Long vehicleId, LocalDate dispatchDate,
            LocalDate deliveryDate, DispatchStatus status, String deliveryAddress, String receivedBy) {
        return new Dispatch(id, orderId, vehicleId, dispatchDate, deliveryDate, status, deliveryAddress, receivedBy);
    }

    public void markInTransit() {
        ensureStatus(DispatchStatus.SCHEDULED);
        this.status = DispatchStatus.IN_TRANSIT;
    }

    public void markDelivered(LocalDate deliveryDate, String receivedBy) {
        ensureStatus(DispatchStatus.IN_TRANSIT);
        this.deliveryDate = requireNonNull(deliveryDate, "delivery date");
        this.receivedBy = requireText(receivedBy, "received by");
        this.status = DispatchStatus.DELIVERED;
    }

    public void cancel() {
        ensureStatus(DispatchStatus.SCHEDULED);
        this.status = DispatchStatus.CANCELLED;
    }

    private void ensureStatus(DispatchStatus expectedStatus) {
        if (status != expectedStatus) throw new IllegalStateException("invalid dispatch status transition");
    }

    public Long id() { return id; }
    public Long orderId() { return orderId; }
    public Long vehicleId() { return vehicleId; }
    public LocalDate dispatchDate() { return dispatchDate; }
    public LocalDate deliveryDate() { return deliveryDate; }
    public DispatchStatus status() { return status; }
    public String deliveryAddress() { return deliveryAddress; }
    public String receivedBy() { return receivedBy; }
}
