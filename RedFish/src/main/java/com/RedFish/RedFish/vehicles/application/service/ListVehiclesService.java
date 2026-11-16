package com.RedFish.RedFish.vehicles.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

@Service
public class ListVehiclesService {

    private final VehicleRepositoryPort repository;

    public ListVehiclesService(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> list() {
        return repository.findAll();
    }
}
