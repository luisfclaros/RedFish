package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;

@Repository
public class JpaDispatchRepository implements DispatchRepositoryPort {
    private final SpringDataDispatchJpaRepository repository;
    public JpaDispatchRepository(SpringDataDispatchJpaRepository repository) { this.repository=repository; }
    @Override public Dispatch save(Dispatch dispatch) {
        return repository.save(DispatchJpaEntity.fromDomain(dispatch)).toDomain();
    }
    @Override public Optional<Dispatch> findById(Long id) {
        return repository.findById(id).map(DispatchJpaEntity::toDomain);
    }
    @Override public List<Dispatch> findAll() {
        return repository.findAll().stream().map(DispatchJpaEntity::toDomain).toList();
    }
    @Override public boolean existsActiveByOrderId(Long orderId) {
        return repository.existsByOrderIdAndStatusNot(orderId, DispatchStatus.CANCELLED);
    }
}
