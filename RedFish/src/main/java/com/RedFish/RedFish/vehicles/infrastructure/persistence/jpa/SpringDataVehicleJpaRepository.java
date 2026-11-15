package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface SpringDataVehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {
    boolean existsByPlateIgnoreCase(String plate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from VehicleJpaEntity v where v.id = :id")
    Optional<VehicleJpaEntity> findByIdForUpdate(@Param("id") Long id);
}
