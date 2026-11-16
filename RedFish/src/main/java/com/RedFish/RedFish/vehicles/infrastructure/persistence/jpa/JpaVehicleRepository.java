package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

@Repository
public class JpaVehicleRepository implements VehicleRepositoryPort {

    private final SpringDataVehicleJpaRepository repository;

    public JpaVehicleRepository(SpringDataVehicleJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id).map(VehicleJpaEntity::toDomain);
    }

    @Override
    public List<Vehicle> findAll() {
        return repository.findAll().stream().map(VehicleJpaEntity::toDomain).toList();
    }

    @Override
    public boolean existsByPlate(String plate) {
        return repository.existsByPlateIgnoreCase(plate);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return repository.save(VehicleJpaEntity.fromDomain(vehicle)).toDomain();
    }
}
