package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Order;

@Repository
public class JpaOrderRepository implements OrderRepositoryPort {
    private final SpringDataOrderJpaRepository repository;
    public JpaOrderRepository(SpringDataOrderJpaRepository repository) { this.repository=repository; }
    @Override @Transactional
    public Order save(Order order) { return repository.save(OrderJpaEntity.fromDomain(order)).toDomain(); }
    @Override @Transactional(readOnly=true)
    public Optional<Order> findById(Long id) { return repository.findById(id).map(OrderJpaEntity::toDomain); }
    @Override @Transactional(readOnly=true)
    public List<Order> findAll() { return repository.findAll().stream().map(OrderJpaEntity::toDomain).toList(); }
}
