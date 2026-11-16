package com.RedFish.RedFish.orders.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Service
public class GetOrderService {
    private final OrderRepositoryPort repository;
    public GetOrderService(OrderRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true)
    public Order get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("order not found"));
    }
}
