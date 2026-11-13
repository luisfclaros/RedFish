package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementPort;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementRepositoryPort;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;

@Component
public class JpaInventoryMovementAdapter implements InventoryMovementPort {
    private final InventoryRepositoryPort inventory;
    private final InventoryMovementRepositoryPort movements;
    public JpaInventoryMovementAdapter(InventoryRepositoryPort inventory, InventoryMovementRepositoryPort movements) {
        this.inventory=inventory; this.movements=movements;
    }
    @Override @Transactional
    public void registerEntry(Long productId, BigDecimal quantity, Long registeredBy, String observations) {
        register(productId, quantity, registeredBy, observations, MovementType.ENTRY);
    }
    @Override @Transactional
    public void registerExit(Long productId, BigDecimal quantity, Long registeredBy, String observations) {
        register(productId, quantity, registeredBy, observations, MovementType.EXIT);
    }
    private void register(Long productId, BigDecimal quantity, Long registeredBy, String observations, MovementType type) {
        InventoryItem item=inventory.findByProductIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("inventory stock not registered for product: " + productId));
        if (type == MovementType.ENTRY) item.increase(quantity); else item.decrease(quantity);
        inventory.save(item);
        movements.save(new InventoryMovement(null, item.id(), type, quantity, LocalDateTime.now(), registeredBy, observations));
    }
}
