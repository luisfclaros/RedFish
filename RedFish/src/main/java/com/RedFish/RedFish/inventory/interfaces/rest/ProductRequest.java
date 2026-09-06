package com.RedFish.RedFish.inventory.interfaces.rest;

import com.RedFish.RedFish.inventory.domain.model.ProductType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
		@NotBlank String code,
		@NotBlank String name,
		@NotBlank String unitOfMeasure,
		@NotNull ProductType type) {
}
