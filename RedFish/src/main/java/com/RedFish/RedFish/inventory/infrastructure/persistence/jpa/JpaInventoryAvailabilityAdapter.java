package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.RedFish.RedFish.inventory.application.port.InventoryAvailabilityPort;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;

@Component
public class JpaInventoryAvailabilityAdapter implements InventoryAvailabilityPort {
    private final InventoryRepositoryPort inventory;
    public JpaInventoryAvailabilityAdapter(InventoryRepositoryPort inventory) { this.inventory=inventory; }
    @Override public boolean hasAvailableStock(Long productId, BigDecimal quantity) {
        return inventory.findByProductId(productId)
                .map(item -> item.quantity().compareTo(quantity) >= 0).orElse(false);
    }
}
