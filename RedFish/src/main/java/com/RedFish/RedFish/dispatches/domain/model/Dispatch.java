package com.RedFish.RedFish.dispatches.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.LocalDate;

import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

public class Dispatch {

	private final Long id;
	private final Long orderId;
	private final Long vehicleId;
	private final LocalDate dispatchDate;
	private LocalDate deliveryDate;
	private DispatchStatus status;
	private final String deliveryAddress;
	private String receivedBy;

	public Dispatch(Long id, Long orderId, Vehicle vehicle, LocalDate dispatchDate, String deliveryAddress) {
		requireNonNull(vehicle, "vehicle").ensureCanBeAssigned();
		this.id = id;
		this.orderId = requireNonNull(orderId, "order id");
		this.vehicleId = vehicle.id();
		this.dispatchDate = requireNonNull(dispatchDate, "dispatch date");
		this.deliveryAddress = requireText(deliveryAddress, "delivery address");
		this.status = DispatchStatus.SCHEDULED;
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
		if (status == DispatchStatus.DELIVERED) {
			throw new IllegalStateException("delivered dispatches cannot be cancelled");
		}
		this.status = DispatchStatus.CANCELLED;
	}

	private void ensureStatus(DispatchStatus expectedStatus) {
		if (status != expectedStatus) {
			throw new IllegalStateException("invalid dispatch status transition");
		}
	}

	public Long id() {
		return id;
	}

	public Long orderId() {
		return orderId;
	}

	public Long vehicleId() {
		return vehicleId;
	}

	public DispatchStatus status() {
		return status;
	}
}
