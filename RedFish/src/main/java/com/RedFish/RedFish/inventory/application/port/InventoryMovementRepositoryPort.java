package com.RedFish.RedFish.inventory.application.port;

import java.util.List;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;

public interface InventoryMovementRepositoryPort {
    InventoryMovement save(InventoryMovement movement);
    List<InventoryMovement> findAll();
}
