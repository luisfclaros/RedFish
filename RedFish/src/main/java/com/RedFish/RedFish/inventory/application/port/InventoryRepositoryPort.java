package com.RedFish.RedFish.inventory.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

public interface InventoryRepositoryPort {
    Optional<InventoryItem> findById(Long id);
    Optional<InventoryItem> findByProductId(Long productId);
    /** Loads the stock row under a write lock when supported by the persistence adapter. */
    default Optional<InventoryItem> findByProductIdForUpdate(Long productId) { return findByProductId(productId); }
    List<InventoryItem> findAll();
    InventoryItem save(InventoryItem inventoryItem);
}
