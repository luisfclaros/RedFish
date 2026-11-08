package com.RedFish.RedFish.orders.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Service
public class UpdateOrderStatusService {
    private final OrderRepositoryPort repository;
    private final InventoryReservationPort inventoryReservation;
    public UpdateOrderStatusService(OrderRepositoryPort repository, InventoryReservationPort inventoryReservation) {
        this.repository=repository; this.inventoryReservation=inventoryReservation;
    }
    @Transactional public Order markInPreparation(Long id) {
        Order order=get(id); order.markInPreparation(); return repository.save(order);
    }
    @Transactional public Order markDispatched(Long id) {
        Order order=get(id); order.markDispatched(); return repository.save(order);
    }
    @Transactional public Order cancel(Long id) {
        Order order=get(id);
        order.cancel();
        inventoryReservation.release(order.items());
        return repository.save(order);
    }
    private Order get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("order not found"));
    }
}
