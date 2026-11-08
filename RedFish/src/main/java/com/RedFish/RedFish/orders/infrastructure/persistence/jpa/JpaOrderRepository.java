package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Repository
public class JpaOrderRepository implements OrderRepositoryPort {
    private final SpringDataOrderJpaRepository repository;
    public JpaOrderRepository(SpringDataOrderJpaRepository repository) { this.repository=repository; }
    @Override public Order save(Order order) {
        return repository.save(OrderJpaEntity.fromDomain(order)).toDomain();
    }
    @Override public Optional<Order> findById(Long id) {
        return repository.findById(id).map(OrderJpaEntity::toDomain);
    }
}
