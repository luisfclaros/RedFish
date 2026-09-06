package com.RedFish.RedFish.orders.application.port;

import java.util.List;
import java.util.Optional;

import com.RedFish.RedFish.orders.domain.model.Customer;

public interface CustomerRepositoryPort {

	Customer save(Customer customer);

	List<Customer> findAll();

	Optional<Customer> findById(Long id);

	boolean existsByEmail(String email);
}
