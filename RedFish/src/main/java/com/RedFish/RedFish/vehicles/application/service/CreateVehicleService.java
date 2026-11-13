package com.RedFish.RedFish.vehicles.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

@Service
public class CreateVehicleService {

    private final VehicleRepositoryPort repository;

    public CreateVehicleService(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional
    public Vehicle create(Vehicle vehicle) {
        if (repository.existsByPlate(vehicle.plate())) {
            throw new IllegalArgumentException("vehicle plate already exists");
        }
        return repository.save(vehicle);
    }
}
