package com.RedFish.RedFish.modules.inventory.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

class InventoryItemTests {

	@Test
	void createsInventoryWithNonNegativeQuantityAndMinimumStock() {
		InventoryItem item = new InventoryItem(1L, 10L, new BigDecimal("12.50"), new BigDecimal("5.00"));

		assertThat(item.quantity()).isEqualByComparingTo("12.50");
		assertThat(item.minimumStock()).isEqualByComparingTo("5.00");
	}

	@Test
	void rejectsNegativeInitialQuantity() {
		assertThatThrownBy(() -> new InventoryItem(1L, 10L, new BigDecimal("-1"), BigDecimal.ZERO))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void increasesStockOnlyByPositiveAmounts() {
		InventoryItem item = new InventoryItem(1L, 10L, new BigDecimal("10"), BigDecimal.ZERO);

		item.increase(new BigDecimal("2.50"));

		assertThat(item.quantity()).isEqualByComparingTo("12.50");
		assertThatThrownBy(() -> item.increase(BigDecimal.ZERO))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void decreasesStockWhenSufficientQuantityExists() {
		InventoryItem item = new InventoryItem(1L, 10L, new BigDecimal("10"), BigDecimal.ZERO);

		item.decrease(new BigDecimal("3.25"));

		assertThat(item.quantity()).isEqualByComparingTo("6.75");
	}

	@Test
	void rejectsDecreaseThatWouldMakeStockNegativeWithoutChangingQuantity() {
		InventoryItem item = new InventoryItem(1L, 10L, new BigDecimal("2"), BigDecimal.ZERO);

		assertThatThrownBy(() -> item.decrease(new BigDecimal("3")))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("cannot be negative");

		assertThat(item.quantity()).isEqualByComparingTo("2");
	}

	@Test
	void identifiesStockBelowConfiguredMinimum() {
		InventoryItem item = new InventoryItem(1L, 10L, new BigDecimal("4"), new BigDecimal("5"));

		assertThat(item.belowMinimumStock()).isTrue();

		item.increase(BigDecimal.ONE);

		assertThat(item.belowMinimumStock()).isFalse();
	}
}
