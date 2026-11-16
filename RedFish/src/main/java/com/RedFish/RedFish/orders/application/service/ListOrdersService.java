package com.RedFish.RedFish.orders.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Service
public class ListOrdersService {
    private final OrderRepositoryPort repository;
    public ListOrdersService(OrderRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true) public List<Order> list() { return repository.findAll(); }
}
