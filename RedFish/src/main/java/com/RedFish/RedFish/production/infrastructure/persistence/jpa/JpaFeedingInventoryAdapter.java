package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementPort;
import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.production.application.port.FeedingInventoryPort;

@Component
public class JpaFeedingInventoryAdapter implements FeedingInventoryPort {
    private final ProductRepositoryPort products;
    private final InventoryMovementPort movements;

    public JpaFeedingInventoryAdapter(ProductRepositoryPort products, InventoryMovementPort movements) {
        this.products=products; this.movements=movements;
    }

    @Override
    @Transactional
    public void registerFoodConsumption(Long productId, java.math.BigDecimal quantity, Long registeredBy) {
        var product=products.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("food product not found"));
        product.ensureCanBeUsed();
        if (product.type() != ProductType.INPUT) {
            throw new IllegalArgumentException("feeding records must reference an inventory input product");
        }
        movements.registerExit(productId, quantity, registeredBy, "Consumo de alimento en producción");
    }
}
