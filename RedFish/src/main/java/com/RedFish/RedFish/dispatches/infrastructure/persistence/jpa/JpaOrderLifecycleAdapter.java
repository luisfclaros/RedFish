package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.dispatches.application.port.OrderAvailabilityPort;
import com.RedFish.RedFish.dispatches.application.port.OrderDispatchPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;

@Component
public class JpaOrderLifecycleAdapter implements OrderAvailabilityPort, OrderDispatchPort {
    private final OrderRepositoryPort orders;
    public JpaOrderLifecycleAdapter(OrderRepositoryPort orders) { this.orders=orders; }

    @Override @Transactional
    public void ensureOrderCanBeDispatched(Long orderId) {
        var order=orders.findByIdForUpdate(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
        if (order.status() != OrderStatus.IN_PREPARATION)
            throw new IllegalStateException("only orders in preparation can be scheduled for dispatch");
    }

    @Override @Transactional
    public void markOrderDispatched(Long orderId) {
        var order=orders.findByIdForUpdate(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
        order.markDispatched();
        orders.save(order);
    }
}
