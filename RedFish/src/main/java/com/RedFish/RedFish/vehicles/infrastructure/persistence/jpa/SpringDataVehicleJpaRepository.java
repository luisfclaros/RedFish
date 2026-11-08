package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataVehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {
    boolean existsByPlateIgnoreCase(String plate);
}
