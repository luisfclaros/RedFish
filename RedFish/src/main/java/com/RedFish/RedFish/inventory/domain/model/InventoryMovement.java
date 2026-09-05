package com.RedFish.RedFish.inventory.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requirePositive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventoryMovement(Long id, Long inventoryId, MovementType type, BigDecimal quantity,
		LocalDateTime date, Long registeredBy, String observations) {

	public InventoryMovement {
		requireNonNull(inventoryId, "inventory id");
		requireNonNull(type, "movement type");
		quantity = requirePositive(quantity, "movement quantity");
		requireNonNull(date, "movement date");
		requireNonNull(registeredBy, "registered by");
	}
}
