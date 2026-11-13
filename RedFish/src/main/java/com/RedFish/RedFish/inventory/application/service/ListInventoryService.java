package com.RedFish.RedFish.inventory.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;

@Service
public class ListInventoryService {
    private final InventoryRepositoryPort repository;
    public ListInventoryService(InventoryRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true) public List<InventoryItem> list() { return repository.findAll(); }
}
