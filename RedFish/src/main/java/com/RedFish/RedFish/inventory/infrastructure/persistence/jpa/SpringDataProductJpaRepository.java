package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

	boolean existsByCodeIgnoreCase(String code);
}
