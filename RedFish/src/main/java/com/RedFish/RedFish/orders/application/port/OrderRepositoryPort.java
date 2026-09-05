package com.RedFish.RedFish.orders.application.port;

import java.util.Optional;

import com.RedFish.RedFish.orders.domain.model.Order;

public interface OrderRepositoryPort {

	Order save(Order order);

	Optional<Order> findById(Long id);
}
