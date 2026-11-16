package com.RedFish.RedFish.inventory.application.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

@Service
public class RegisterInventoryStockService {
    private final InventoryRepositoryPort inventory;
    private final ProductRepositoryPort products;
    public RegisterInventoryStockService(InventoryRepositoryPort inventory, ProductRepositoryPort products) {
        this.inventory=inventory; this.products=products;
    }
    @Transactional
    public InventoryItem register(Long productId, BigDecimal initialQuantity, BigDecimal minimumStock) {
        var product=products.findById(productId).orElseThrow(() -> new IllegalArgumentException("product not found"));
        product.ensureCanBeUsed();
        if (inventory.findByProductId(productId).isPresent())
            throw new IllegalArgumentException("inventory stock already registered for product");
        return inventory.save(new InventoryItem(null, productId, initialQuantity, minimumStock));
    }
}
