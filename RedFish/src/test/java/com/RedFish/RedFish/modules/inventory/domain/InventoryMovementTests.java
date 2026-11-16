package com.RedFish.RedFish.modules.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;

class InventoryMovementTests {

	@Test
	void createsAnEntryMovementWithRequiredAuditData() {
		LocalDateTime date = LocalDateTime.of(2026, 11, 3, 10, 30);

		InventoryMovement movement = new InventoryMovement(1L, 2L, MovementType.ENTRY,
				new BigDecimal("15.50"), date, 7L, "Initial stock");

		assertThat(movement.inventoryId()).isEqualTo(2L);
		assertThat(movement.type()).isEqualTo(MovementType.ENTRY);
		assertThat(movement.quantity()).isEqualByComparingTo("15.50");
		assertThat(movement.date()).isEqualTo(date);
		assertThat(movement.registeredBy()).isEqualTo(7L);
	}

	@Test
	void createsAnExitMovement() {
		InventoryMovement movement = new InventoryMovement(2L, 2L, MovementType.EXIT,
				BigDecimal.ONE, LocalDateTime.now(), 7L, "Order dispatch");

		assertThat(movement.type()).isEqualTo(MovementType.EXIT);
		assertThat(movement.quantity()).isEqualByComparingTo("1");
	}

	@Test
	void rejectsNonPositiveMovementQuantity() {
		assertThatThrownBy(() -> new InventoryMovement(1L, 2L, MovementType.ENTRY,
				BigDecimal.ZERO, LocalDateTime.now(), 7L, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("greater than zero");
	}

	@Test
	void requiresInventoryMovementTypeDateAndResponsibleUser() {
		LocalDateTime date = LocalDateTime.now();

		assertThatThrownBy(() -> new InventoryMovement(1L, null, MovementType.ENTRY,
				BigDecimal.ONE, date, 7L, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("inventory id");

		assertThatThrownBy(() -> new InventoryMovement(1L, 2L, null,
				BigDecimal.ONE, date, 7L, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("movement type");

		assertThatThrownBy(() -> new InventoryMovement(1L, 2L, MovementType.EXIT,
				BigDecimal.ONE, null, 7L, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("movement date");

		assertThatThrownBy(() -> new InventoryMovement(1L, 2L, MovementType.EXIT,
				BigDecimal.ONE, date, null, null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("registered by");
	}
}
