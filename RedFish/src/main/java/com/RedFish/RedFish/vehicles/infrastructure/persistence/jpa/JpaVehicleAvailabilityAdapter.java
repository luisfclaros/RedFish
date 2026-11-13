package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

import org.springframework.stereotype.Component;

@Component
public class JpaVehicleAvailabilityAdapter implements VehicleAvailabilityPort {

    private final VehicleRepositoryPort vehicles;

    public JpaVehicleAvailabilityAdapter(VehicleRepositoryPort vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void ensureVehicleCanBeAssigned(Long vehicleId) {
        Vehicle vehicle = vehicles.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("vehicle not found"));
        vehicle.ensureCanBeAssigned();
    }
}
