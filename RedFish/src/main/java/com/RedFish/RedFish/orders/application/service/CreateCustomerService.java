package com.RedFish.RedFish.orders.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

@Service
public class CreateCustomerService {

	private final CustomerRepositoryPort customerRepository;

	public CreateCustomerService(CustomerRepositoryPort customerRepository) {
		this.customerRepository = requireNonNull(customerRepository, "customer repository");
	}

	public Customer create(Customer customer) {
		Customer requestedCustomer = requireNonNull(customer, "customer");
		if (requestedCustomer.email() != null && !requestedCustomer.email().isBlank()
				&& customerRepository.existsByEmail(requestedCustomer.email())) {
			throw new IllegalArgumentException("customer email already exists");
		}
		return customerRepository.save(requestedCustomer);
	}
}
