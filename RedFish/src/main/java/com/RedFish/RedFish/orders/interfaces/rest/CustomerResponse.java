package com.RedFish.RedFish.orders.interfaces.rest;

import com.RedFish.RedFish.orders.domain.model.Customer;

public record CustomerResponse(Long id, String name, String phone, String address, String email, boolean active) {

	public static CustomerResponse from(Customer customer) {
		return new CustomerResponse(customer.id(), customer.name(), customer.phone(), customer.address(),
				customer.email(), customer.active());
	}
}
