package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;

@Repository
public class JpaInventoryMovementRepository implements InventoryMovementRepositoryPort {
    private final SpringDataInventoryMovementJpaRepository repository;
    public JpaInventoryMovementRepository(SpringDataInventoryMovementJpaRepository repository) { this.repository=repository; }
    @Override public InventoryMovement save(InventoryMovement movement) {
        return repository.save(InventoryMovementJpaEntity.fromDomain(movement)).toDomain();
    }
    @Override public List<InventoryMovement> findAll() {
        return repository.findAll().stream().map(InventoryMovementJpaEntity::toDomain).toList();
    }
}
