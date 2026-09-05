package com.RedFish.RedFish.orders.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

public class Customer {

	private final Long id;
	private final String name;
	private final String phone;
	private final String address;
	private final String email;
	private boolean active;

	public Customer(Long id, String name, String phone, String address, String email, boolean active) {
		this.id = id;
		this.name = requireText(name, "customer name");
		this.phone = requireText(phone, "customer phone");
		this.address = requireText(address, "customer address");
		this.email = email;
		this.active = active;
	}

	public void ensureCanPlaceOrders() {
		if (!active) {
			throw new IllegalStateException("inactive customers cannot place orders");
		}
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public boolean active() {
		return active;
	}
}
