package com.RedFish.RedFish.orders.application.service;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

@Service
public class GetCustomerService {

	private final CustomerRepositoryPort customerRepository;

	public GetCustomerService(CustomerRepositoryPort customerRepository) {
		this.customerRepository = customerRepository;
	}

	public Customer get(Long id) {
		return customerRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("customer not found"));
	}
}
