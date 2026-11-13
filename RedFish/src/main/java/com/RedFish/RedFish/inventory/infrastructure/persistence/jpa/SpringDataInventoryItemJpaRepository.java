package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface SpringDataInventoryItemJpaRepository extends JpaRepository<InventoryItemJpaEntity, Long> {
    boolean existsByProductId(Long productId);
    Optional<InventoryItemJpaEntity> findByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryItemJpaEntity i where i.productId = :productId")
    Optional<InventoryItemJpaEntity> findByProductIdForUpdate(@Param("productId") Long productId);
}
