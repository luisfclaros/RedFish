package com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa;

import java.math.BigDecimal;

import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vehiculos")
public class VehicleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String plate;

    @Column(nullable = false, length = 60)
    private String brand;

    @Column(nullable = false, length = 60)
    private String model;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal loadCapacity;

    @Column(nullable = false)
    private boolean active;

    @Column(length = 500)
    private String observations;

    protected VehicleJpaEntity() {}

    private VehicleJpaEntity(Long id, String plate, String brand, String model, BigDecimal loadCapacity,
            boolean active, String observations) {
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.loadCapacity = loadCapacity;
        this.active = active;
        this.observations = observations;
    }

    public static VehicleJpaEntity fromDomain(Vehicle vehicle) {
        return new VehicleJpaEntity(vehicle.id(), vehicle.plate(), vehicle.brand(), vehicle.model(),
                vehicle.loadCapacity(), vehicle.active(), vehicle.observations());
    }

    public Vehicle toDomain() {
        return new Vehicle(id, plate, brand, model, loadCapacity, active, observations);
    }
}
