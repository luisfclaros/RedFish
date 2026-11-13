package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;

public interface SpringDataDispatchJpaRepository extends JpaRepository<DispatchJpaEntity, Long> {
    boolean existsByVehicleId(Long vehicleId);
    boolean existsByOrderIdAndStatusNot(Long orderId, DispatchStatus excludedStatus);
    boolean existsByVehicleIdAndStatusIn(Long vehicleId, Collection<DispatchStatus> statuses);
}
