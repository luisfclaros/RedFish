package com.RedFish.RedFish.modules.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.application.service.CreateOrderService;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;

class CreateOrderServiceTests {

	private Order sampleOrder() {
		Customer customer = new Customer(10L, "Restaurante El Lago", "3001234567",
				"Calle 10 # 15-20", "compras@ellago.com", true);
		return new Order(null, customer, LocalDate.of(2026, 11, 3),
				List.of(new OrderItem(20L, "Tilapia Roja", new BigDecimal("5"), new BigDecimal("12000"))),
				"Test order");
	}

	@Test
	void reservesInventoryBeforePersistingOrder() {
		AtomicBoolean reserved = new AtomicBoolean(false);
		AtomicReference<Order> savedOrder = new AtomicReference<>();

		InventoryReservationPort reservation = items -> {
			assertThat(items).hasSize(1);
			assertThat(items.get(0).productId()).isEqualTo(20L);
			reserved.set(true);
		};
		OrderRepositoryPort repository = new OrderRepositoryPort() {
			@Override
			public Order save(Order order) {
				assertThat(reserved).isTrue();
				savedOrder.set(order);
				return order;
			}

			@Override
			public Optional<Order> findById(Long id) {
				return Optional.empty();
			}
		};

		Order result = new CreateOrderService(repository, reservation).create(sampleOrder());

		assertThat(result).isSameAs(savedOrder.get());
		assertThat(reserved).isTrue();
	}

	@Test
	void doesNotPersistOrderWhenInventoryReservationFails() {
		AtomicBoolean persisted = new AtomicBoolean(false);
		InventoryReservationPort reservation = items -> {
			throw new IllegalStateException("insufficient inventory");
		};
		OrderRepositoryPort repository = new OrderRepositoryPort() {
			@Override
			public Order save(Order order) {
				persisted.set(true);
				return order;
			}

			@Override
			public Optional<Order> findById(Long id) {
				return Optional.empty();
			}
		};

		assertThatThrownBy(() -> new CreateOrderService(repository, reservation).create(sampleOrder()))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("insufficient inventory");
		assertThat(persisted).isFalse();
	}

	@Test
	void rejectsNullOrderBeforeCallingCollaborators() {
		AtomicBoolean reserved = new AtomicBoolean(false);
		InventoryReservationPort reservation = items -> reserved.set(true);
		OrderRepositoryPort repository = new OrderRepositoryPort() {
			@Override
			public Order save(Order order) {
				return order;
			}

			@Override
			public Optional<Order> findById(Long id) {
				return Optional.empty();
			}
		};

		assertThatThrownBy(() -> new CreateOrderService(repository, reservation).create(null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("order");
		assertThat(reserved).isFalse();
	}
}
