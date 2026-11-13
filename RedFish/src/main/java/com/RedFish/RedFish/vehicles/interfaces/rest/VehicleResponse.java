package com.RedFish.RedFish.vehicles.interfaces.rest;

import java.math.BigDecimal;

import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

public record VehicleResponse(Long id, String plate, String brand, String model,
        BigDecimal loadCapacity, boolean active, String observations) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(vehicle.id(), vehicle.plate(), vehicle.brand(), vehicle.model(),
                vehicle.loadCapacity(), vehicle.active(), vehicle.observations());
    }
}
