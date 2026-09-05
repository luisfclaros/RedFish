package com.RedFish.RedFish.dispatches.application.port;

public interface VehicleAvailabilityPort {

	void ensureVehicleCanBeAssigned(Long vehicleId);
}
