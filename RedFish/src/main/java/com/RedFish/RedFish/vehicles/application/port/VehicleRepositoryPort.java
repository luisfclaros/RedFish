package com.RedFish.RedFish.vehicles.application.port;

import java.util.List;
import java.util.Optional;

import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

public interface VehicleRepositoryPort {

    Optional<Vehicle> findById(Long id);

    List<Vehicle> findAll();

    boolean existsByPlate(String plate);

    Vehicle save(Vehicle vehicle);
}
