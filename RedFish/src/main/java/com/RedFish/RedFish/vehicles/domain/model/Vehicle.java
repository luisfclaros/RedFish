package com.RedFish.RedFish.vehicles.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNegative;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.math.BigDecimal;

public class Vehicle {

	private final Long id;
	private final String plate;
	private final String brand;
	private final String model;
	private final BigDecimal loadCapacity;
	private boolean active;
	private final String observations;

	public Vehicle(Long id, String plate, String brand, String model, BigDecimal loadCapacity, boolean active,
			String observations) {
		this.id = id;
		this.plate = requireText(plate, "vehicle plate");
		this.brand = requireText(brand, "vehicle brand");
		this.model = requireText(model, "vehicle model");
		this.loadCapacity = requireNonNegative(loadCapacity, "load capacity");
		this.active = active;
		this.observations = observations;
	}

	public void ensureCanBeAssigned() {
		if (!active) {
			throw new IllegalStateException("inactive vehicles cannot be assigned");
		}
	}

	public Long id() {
		return id;
	}

	public String plate() {
		return plate;
	}

	public boolean active() {
		return active;
	}
}
