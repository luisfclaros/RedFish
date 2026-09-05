package com.RedFish.RedFish.dispatches.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.LocalDate;

import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

public class ScheduleDispatchService {

	private final DispatchRepositoryPort dispatchRepository;
	private final VehicleAvailabilityPort vehicleAvailability;

	public ScheduleDispatchService(DispatchRepositoryPort dispatchRepository, VehicleAvailabilityPort vehicleAvailability) {
		this.dispatchRepository = requireNonNull(dispatchRepository, "dispatch repository");
		this.vehicleAvailability = requireNonNull(vehicleAvailability, "vehicle availability");
	}

	public Dispatch schedule(Long orderId, Long vehicleId, LocalDate dispatchDate, String deliveryAddress) {
		vehicleAvailability.ensureVehicleCanBeAssigned(vehicleId);
		Dispatch dispatch = new Dispatch(null, requireNonNull(orderId, "order id"), requireNonNull(vehicleId, "vehicle id"),
				requireNonNull(dispatchDate, "dispatch date"), requireText(deliveryAddress, "delivery address"));
		return dispatchRepository.save(dispatch);
	}
}
