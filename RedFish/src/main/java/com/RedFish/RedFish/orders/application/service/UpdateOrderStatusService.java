package com.RedFish.RedFish.orders.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.DispatchAvailabilityPort;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Service
public class UpdateOrderStatusService {
    private final OrderRepositoryPort repository;
    private final InventoryReservationPort inventoryReservation;
    private final DispatchAvailabilityPort dispatchAvailability;

    public UpdateOrderStatusService(OrderRepositoryPort repository, InventoryReservationPort inventoryReservation) {
        this(repository, inventoryReservation, orderId -> {});
    }

    @Autowired
    public UpdateOrderStatusService(OrderRepositoryPort repository, InventoryReservationPort inventoryReservation,
            DispatchAvailabilityPort dispatchAvailability) {
        this.repository = repository;
        this.inventoryReservation = inventoryReservation;
        this.dispatchAvailability = dispatchAvailability;
    }

    @Transactional public Order markInPreparation(Long id) {
        Order order = getForUpdate(id);
        order.markInPreparation();
        return repository.save(order);
    }

    @Transactional public Order markDispatched(Long id) {
        Order order = getForUpdate(id);
        order.markDispatched();
        return repository.save(order);
    }

    @Transactional public Order cancel(Long id) {
        Order order = getForUpdate(id);
        dispatchAvailability.ensureNoActiveDispatch(id);
        order.cancel();
        inventoryReservation.release(order.items());
        return repository.save(order);
    }

    private Order getForUpdate(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
    }
}
