package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
        this.inventory = inventory;
        this.products = products;
    }

    @Override
    @Transactional
    public void reserve(List<OrderItem> items) {
        Map<Long, BigDecimal> requestedByProduct = aggregate(items);
        Map<Long, InventoryItem> lockedStocks = new TreeMap<>();

        // Lock stock rows in product-id order to prevent overselling and reduce deadlock risk.
        // Validate every line before changing any stock.
        for (Map.Entry<Long, BigDecimal> request : requestedByProduct.entrySet()) {
            Product product = products.findById(request.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("product not found: " + request.getKey()));
            product.ensureCanBeUsed();
            if (product.type() != ProductType.PRODUCT) {
                throw new IllegalArgumentException("orders can only contain saleable products");
            }
            InventoryItem stock = inventory.findByProductIdForUpdate(request.getKey())
                    .orElseThrow(() -> new IllegalStateException(
                            "inventory stock not registered for product: " + request.getKey()));
            if (stock.quantity().compareTo(request.getValue()) < 0) {
                throw new IllegalStateException("insufficient inventory for product: " + request.getKey());
            }
            lockedStocks.put(request.getKey(), stock);
        }

        for (Map.Entry<Long, BigDecimal> request : requestedByProduct.entrySet()) {
            InventoryItem stock = lockedStocks.get(request.getKey());
            stock.decrease(request.getValue());
            inventory.save(stock);
        }
    }

    @Override
    @Transactional
    public void release(List<OrderItem> items) {
        Map<Long, BigDecimal> releasedByProduct = aggregate(items);
        for (Map.Entry<Long, BigDecimal> release : releasedByProduct.entrySet()) {
            InventoryItem stock = inventory.findByProductIdForUpdate(release.getKey())
                    .orElseThrow(() -> new IllegalStateException(
                            "inventory stock not registered for product: " + release.getKey()));
            stock.increase(release.getValue());
            inventory.save(stock);
        }
    }

    private Map<Long, BigDecimal> aggregate(List<OrderItem> items) {
        Map<Long, BigDecimal> quantities = new TreeMap<>();
        for (OrderItem item : items) {
            quantities.merge(item.productId(), item.quantity(), BigDecimal::add);
        }
        return quantities;
    }
}
