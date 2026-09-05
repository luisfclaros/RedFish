package com.RedFish.RedFish.vehicles.application.port;

import java.util.Optional;

import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

public interface VehicleRepositoryPort {

	Optional<Vehicle> findById(Long id);

	Vehicle save(Vehicle vehicle);
}
