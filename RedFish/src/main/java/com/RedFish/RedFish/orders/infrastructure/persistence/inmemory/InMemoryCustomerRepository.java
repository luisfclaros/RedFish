package com.RedFish.RedFish.orders.infrastructure.persistence.inmemory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

public class InMemoryCustomerRepository implements CustomerRepositoryPort {

	private final ConcurrentMap<Long, Customer> customers = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0);

	@Override
	public Customer save(Customer customer) {
		Long id = customer.id() == null ? sequence.incrementAndGet() : customer.id();
		Customer storedCustomer = new Customer(id, customer.name(), customer.phone(), customer.address(),
				customer.email(), customer.active());
		customers.put(id, storedCustomer);
		return storedCustomer;
	}

	@Override
	public List<Customer> findAll() {
		return customers.values()
			.stream()
			.sorted(Comparator.comparing(Customer::id))
			.toList();
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return Optional.ofNullable(customers.get(id));
	}

	@Override
	public boolean existsByEmail(String email) {
		return customers.values()
			.stream()
			.anyMatch(customer -> customer.email() != null && customer.email().equalsIgnoreCase(email));
	}
}
