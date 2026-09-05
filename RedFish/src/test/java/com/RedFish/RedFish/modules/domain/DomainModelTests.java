package com.RedFish.RedFish.modules.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

class DomainModelTests {

	@Test
	void inventoryCannotBecomeNegative() {
		InventoryItem inventory = new InventoryItem(1L, 10L, new BigDecimal("5.00"), new BigDecimal("2.00"));

		assertThatThrownBy(() -> inventory.decrease(new BigDecimal("6.00")))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("stock cannot be negative");
	}

	@Test
	void orderCalculatesTotalFromItems() {
		Customer customer = new Customer(1L, "Piscicola Norte", "3001234567", "Via principal", null, true);
		Order order = new Order(1L, customer, LocalDate.now(),
				List.of(new OrderItem(1L, "Tilapia", new BigDecimal("3.00"), new BigDecimal("12000.00")),
						new OrderItem(2L, "Trucha", new BigDecimal("2.00"), new BigDecimal("15000.00"))),
				null);

		assertThat(order.total()).isEqualByComparingTo(new BigDecimal("66000.00"));
		assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
	}

	@Test
	void inactivePondCannotReceiveFeedingRecord() {
		Pond pond = new Pond(1L, "EST-001", "Estanque Norte", "Zona A", new BigDecimal("100.00"), false);

		assertThatThrownBy(() -> FeedingRecord.registerFor(pond, LocalDate.now(), "Concentrado",
				new BigDecimal("8.00"), 1L, null))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("inactive ponds");
	}

	@Test
	void inactiveVehicleCannotBeAssignedToDispatch() {
		Vehicle vehicle = new Vehicle(1L, "ABC123", "Chevrolet", "NPR", new BigDecimal("1200.00"), false, null);

		assertThatThrownBy(() -> new Dispatch(1L, 20L, vehicle, LocalDate.now(), "Bodega principal"))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("inactive vehicles");
	}
}
