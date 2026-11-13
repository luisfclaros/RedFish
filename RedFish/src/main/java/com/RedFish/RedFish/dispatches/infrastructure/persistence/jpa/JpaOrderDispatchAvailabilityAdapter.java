package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import org.springframework.stereotype.Component;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.orders.application.port.DispatchAvailabilityPort;

@Component
public class JpaOrderDispatchAvailabilityAdapter implements DispatchAvailabilityPort {
    private final DispatchRepositoryPort dispatches;

    public JpaOrderDispatchAvailabilityAdapter(DispatchRepositoryPort dispatches) {
        this.dispatches = dispatches;
    }

    @Override
    public void ensureNoActiveDispatch(Long orderId) {
        if (dispatches.existsActiveByOrderId(orderId)) {
            throw new IllegalStateException("orders with an active dispatch cannot be cancelled");
        }
    }
}
