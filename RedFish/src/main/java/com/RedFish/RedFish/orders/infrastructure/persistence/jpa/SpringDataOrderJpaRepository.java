package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

public interface SpringDataOrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {
    boolean existsByCustomerId(Long customerId);

    @Query("select case when count(o) > 0 then true else false end from OrderJpaEntity o join o.items i where i.productId = :productId")
    boolean existsAnyItemForProductId(@Param("productId") Long productId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderJpaEntity o where o.id = :id")
    Optional<OrderJpaEntity> findByIdForUpdate(@Param("id") Long id);
}
