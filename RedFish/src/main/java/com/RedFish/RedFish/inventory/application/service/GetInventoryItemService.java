package com.RedFish.RedFish.inventory.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

@Service
public class GetInventoryItemService {
    private final InventoryRepositoryPort repository;
    public GetInventoryItemService(InventoryRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true)
    public InventoryItem get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("inventory item not found"));
    }
}
