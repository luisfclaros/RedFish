package com.RedFish.RedFish.inventory.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

public interface InventoryRepositoryPort {
    Optional<InventoryItem> findByProductId(Long productId);
    List<InventoryItem> findAll();
    InventoryItem save(InventoryItem inventoryItem);
}
