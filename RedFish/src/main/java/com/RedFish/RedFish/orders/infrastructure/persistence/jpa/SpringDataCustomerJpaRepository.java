package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {

	boolean existsByEmailIgnoreCase(String email);
}
