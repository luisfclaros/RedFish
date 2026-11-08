package com.RedFish.RedFish.vehicles.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

@Service
public class GetVehicleService {

    private final VehicleRepositoryPort repository;

    public GetVehicleService(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Vehicle get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("vehicle not found"));
    }
}
