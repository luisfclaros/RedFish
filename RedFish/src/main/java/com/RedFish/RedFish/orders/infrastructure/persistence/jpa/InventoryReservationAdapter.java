package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.domain.model.OrderItem;

@Component
public class InventoryReservationAdapter implements InventoryReservationPort {
    private final InventoryRepositoryPort inventory;
    private final ProductRepositoryPort products;
    public InventoryReservationAdapter(InventoryRepositoryPort inventory, ProductRepositoryPort products) {
        this.inventory=inventory; this.products=products;
    }

    @Override
    @Transactional
    public void reserve(List<OrderItem> items) {
        for (OrderItem line : items) {
            Product product=products.findById(line.productId())
                    .orElseThrow(() -> new IllegalArgumentException("product not found: " + line.productId()));
            product.ensureCanBeUsed();
            if (product.type() != ProductType.PRODUCT) {
                throw new IllegalArgumentException("orders can only contain saleable products");
            }
            InventoryItem stock=inventory.findByProductId(line.productId())
                    .orElseThrow(() -> new IllegalStateException("inventory stock not registered for product: " + line.productId()));
            if (stock.quantity().compareTo(line.quantity()) < 0) {
                throw new IllegalStateException("insufficient inventory for product: " + line.productId());
            }
        }
        for (OrderItem line : items) {
            InventoryItem stock=inventory.findByProductId(line.productId()).orElseThrow();
            stock.decrease(line.quantity());
            inventory.save(stock);
        }
    }
}
