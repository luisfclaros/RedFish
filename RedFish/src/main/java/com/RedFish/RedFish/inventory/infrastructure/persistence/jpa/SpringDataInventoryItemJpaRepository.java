package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInventoryItemJpaRepository extends JpaRepository<InventoryItemJpaEntity, Long> {
    Optional<InventoryItemJpaEntity> findByProductId(Long productId);
}
