package com.RedFish.RedFish.modules.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.dispatches.application.service.ScheduleDispatchService;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.application.service.CreateOrderService;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;

class ApplicationContractTests {

	@Test
	void createOrderUsesInventoryReservationBeforeSaving() {
		AtomicBoolean inventoryWasReserved = new AtomicBoolean(false);
		AtomicReference<Order> savedOrder = new AtomicReference<>();
		Order order = sampleOrder();
		InventoryReservationPort inventoryReservation = items -> inventoryWasReserved.set(true);
		OrderRepositoryPort orderRepository = new OrderRepositoryPort() {
			@Override
			public Order save(Order order) {
				savedOrder.set(order);
				return order;
			}

			@Override
			public Optional<Order> findById(Long id) {
				return Optional.empty();
			}
		};

		Order result = new CreateOrderService(orderRepository, inventoryReservation).create(order);

		assertThat(inventoryWasReserved).isTrue();
		assertThat(savedOrder.get()).isSameAs(order);
		assertThat(result).isSameAs(order);
	}

	@Test
	void scheduleDispatchUsesVehicleAvailabilityBeforeSaving() {
		AtomicBoolean vehicleWasChecked = new AtomicBoolean(false);
		DispatchRepositoryPort dispatchRepository = new InMemoryDispatchRepository();
		VehicleAvailabilityPort vehicleAvailability = vehicleId -> vehicleWasChecked.set(true);

		Dispatch dispatch = new ScheduleDispatchService(dispatchRepository, vehicleAvailability)
			.schedule(10L, 5L, LocalDate.now(), "Bodega principal");

		assertThat(vehicleWasChecked).isTrue();
		assertThat(dispatch.orderId()).isEqualTo(10L);
		assertThat(dispatch.vehicleId()).isEqualTo(5L);
	}

	@Test
	void scheduleDispatchStopsWhenVehicleCannotBeAssigned() {
		DispatchRepositoryPort dispatchRepository = new InMemoryDispatchRepository();
		VehicleAvailabilityPort vehicleAvailability = vehicleId -> {
			throw new IllegalStateException("vehicle is not available");
		};

		assertThatThrownBy(() -> new ScheduleDispatchService(dispatchRepository, vehicleAvailability)
			.schedule(10L, 5L, LocalDate.now(), "Bodega principal"))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("vehicle is not available");
	}

	private static Order sampleOrder() {
		Customer customer = new Customer(1L, "Piscicola Norte", "3001234567", "Via principal", null, true);
		return new Order(1L, customer, LocalDate.now(),
				List.of(new OrderItem(1L, "Tilapia", new BigDecimal("3.00"), new BigDecimal("12000.00"))),
				null);
	}

	private static class InMemoryDispatchRepository implements DispatchRepositoryPort {

		@Override
		public Dispatch save(Dispatch dispatch) {
			return dispatch;
		}

		@Override
		public Optional<Dispatch> findById(Long id) {
			return Optional.empty();
		}
	}
}
