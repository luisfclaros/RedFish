package com.RedFish.RedFish.orders.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

@Service
public class ListCustomersService {

	private final CustomerRepositoryPort customerRepository;

	public ListCustomersService(CustomerRepositoryPort customerRepository) {
		this.customerRepository = customerRepository;
	}

	public List<Customer> list() {
		return customerRepository.findAll();
	}
}
