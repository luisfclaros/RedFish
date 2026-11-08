package com.RedFish.RedFish.inventory.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;

public record InventoryMovementResponse(Long id, Long inventoryId, MovementType type, BigDecimal quantity,
        LocalDateTime date, Long registeredBy, String observations) {
    public static InventoryMovementResponse from(InventoryMovement m) {
        return new InventoryMovementResponse(m.id(), m.inventoryId(), m.type(), m.quantity(), m.date(),
                m.registeredBy(), m.observations());
    }
}
