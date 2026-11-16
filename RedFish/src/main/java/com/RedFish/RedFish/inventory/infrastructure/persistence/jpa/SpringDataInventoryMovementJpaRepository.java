package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInventoryMovementJpaRepository extends JpaRepository<InventoryMovementJpaEntity, Long> {}
