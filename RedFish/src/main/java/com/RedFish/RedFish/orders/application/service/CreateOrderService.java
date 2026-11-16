package com.RedFish.RedFish.orders.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Service
public class CreateOrderService {
    private final OrderRepositoryPort orderRepository;
    private final InventoryReservationPort inventoryReservation;

    public CreateOrderService(OrderRepositoryPort orderRepository, InventoryReservationPort inventoryReservation) {
        this.orderRepository = requireNonNull(orderRepository, "order repository");
        this.inventoryReservation = requireNonNull(inventoryReservation, "inventory reservation");
    }

    @Transactional
    public Order create(Order order) {
        Order requestedOrder = requireNonNull(order, "order");
        inventoryReservation.reserve(requestedOrder.items());
        return orderRepository.save(requestedOrder);
    }
}
