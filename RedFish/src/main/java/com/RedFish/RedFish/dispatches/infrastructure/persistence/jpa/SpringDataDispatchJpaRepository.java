package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;

public interface SpringDataDispatchJpaRepository extends JpaRepository<DispatchJpaEntity, Long> {
    boolean existsByOrderIdAndStatusNot(Long orderId, DispatchStatus excludedStatus);
}
