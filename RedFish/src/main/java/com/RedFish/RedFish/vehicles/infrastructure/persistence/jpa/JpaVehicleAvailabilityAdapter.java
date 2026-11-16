package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

import org.springframework.stereotype.Component;

@Component
public class JpaVehicleAvailabilityAdapter implements VehicleAvailabilityPort {

    private final SpringDataVehicleJpaRepository vehicles;

    public JpaVehicleAvailabilityAdapter(SpringDataVehicleJpaRepository vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public void ensureVehicleCanBeAssigned(Long vehicleId) {
        Vehicle vehicle = vehicles.findByIdForUpdate(vehicleId)
                .map(VehicleJpaEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("vehicle not found"));
        vehicle.ensureCanBeAssigned();
    }
}
