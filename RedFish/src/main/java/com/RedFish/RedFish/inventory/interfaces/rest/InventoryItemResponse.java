package com.RedFish.RedFish.inventory.interfaces.rest;

import java.math.BigDecimal;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

public record InventoryItemResponse(Long id, Long productId, BigDecimal quantity, BigDecimal minimumStock,
        boolean belowMinimumStock) {
    public static InventoryItemResponse from(InventoryItem i) {
        return new InventoryItemResponse(i.id(), i.productId(), i.quantity(), i.minimumStock(), i.belowMinimumStock());
    }
}
