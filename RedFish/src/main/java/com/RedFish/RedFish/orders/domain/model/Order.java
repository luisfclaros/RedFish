package com.RedFish.RedFish.orders.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

	private final Long id;
	private final Long customerId;
	private final LocalDate date;
	private final List<OrderItem> items;
	private OrderStatus status;
	private String observations;

	public Order(Long id, Customer customer, LocalDate date, List<OrderItem> items, String observations) {
		requireNonNull(customer, "customer").ensureCanPlaceOrders();
		this.id = id;
		this.customerId = customer.id();
		this.date = requireNonNull(date, "order date");
		this.items = new ArrayList<>(requireNonNull(items, "order items"));
		this.observations = observations;
		this.status = OrderStatus.PENDING;
		ensureHasItems();
	}

	public void markInPreparation() {
		ensureStatus(OrderStatus.PENDING);
		this.status = OrderStatus.IN_PREPARATION;
	}

	public void markDispatched() {
		ensureStatus(OrderStatus.IN_PREPARATION);
		this.status = OrderStatus.DISPATCHED;
	}

	public void cancel() {
		if (status == OrderStatus.DISPATCHED) {
			throw new IllegalStateException("dispatched orders cannot be cancelled");
		}
		this.status = OrderStatus.CANCELLED;
	}

	public BigDecimal total() {
		return items.stream()
			.map(OrderItem::subtotal)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private void ensureHasItems() {
		if (items.isEmpty()) {
			throw new IllegalArgumentException("orders must have at least one item");
		}
	}

	private void ensureStatus(OrderStatus expectedStatus) {
		if (status != expectedStatus) {
			throw new IllegalStateException("invalid order status transition");
		}
	}

	public Long id() {
		return id;
	}

	public Long customerId() {
		return customerId;
	}

	public LocalDate date() {
		return date;
	}

	public List<OrderItem> items() {
		return Collections.unmodifiableList(items);
	}

	public OrderStatus status() {
		return status;
	}

	public String observations() {
		return observations;
	}
}
