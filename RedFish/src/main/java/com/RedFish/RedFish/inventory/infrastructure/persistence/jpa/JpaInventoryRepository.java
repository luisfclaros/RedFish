package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.util.Optional;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

@Repository
public class JpaInventoryRepository implements InventoryRepositoryPort {
    private final SpringDataInventoryItemJpaRepository repository;
    public JpaInventoryRepository(SpringDataInventoryItemJpaRepository repository) { this.repository=repository; }
    @Override public Optional<InventoryItem> findById(Long id) {
        return repository.findById(id).map(InventoryItemJpaEntity::toDomain);
    }
    @Override public Optional<InventoryItem> findByProductId(Long productId) {
        return repository.findByProductId(productId).map(InventoryItemJpaEntity::toDomain);
    }
    @Override public List<InventoryItem> findAll() {
        return repository.findAll().stream().map(InventoryItemJpaEntity::toDomain).toList();
    }
    @Override public InventoryItem save(InventoryItem item) {
        return repository.save(InventoryItemJpaEntity.fromDomain(item)).toDomain();
    }
}
