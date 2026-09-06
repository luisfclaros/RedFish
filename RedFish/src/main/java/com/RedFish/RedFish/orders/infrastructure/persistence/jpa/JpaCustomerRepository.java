package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

@Repository
public class JpaCustomerRepository implements CustomerRepositoryPort {

	private final SpringDataCustomerJpaRepository repository;

	public JpaCustomerRepository(SpringDataCustomerJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Customer save(Customer customer) {
		return repository.save(CustomerJpaEntity.fromDomain(customer)).toDomain();
	}

	@Override
	public List<Customer> findAll() {
		return repository.findAll().stream().map(CustomerJpaEntity::toDomain).toList();
	}

	@Override
	public Optional<Customer> findById(Long id) {
		return repository.findById(id).map(CustomerJpaEntity::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmailIgnoreCase(email);
	}
}
