package com.RedFish.RedFish.orders.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.orders.domain.model.Order;

public interface OrderRepositoryPort {
    Order save(Order order);
    Optional<Order> findById(Long id);
    /** Loads an order under a write lock when the persistence adapter supports it. */
    default Optional<Order> findByIdForUpdate(Long id) { return findById(id); }
    default List<Order> findAll() { return List.of(); }
}
