package com.RedFish.RedFish.inventory.application.port;

import java.math.BigDecimal;

public interface InventoryMovementPort {

	void registerEntry(Long productId, BigDecimal quantity, Long registeredBy, String observations);

	void registerExit(Long productId, BigDecimal quantity, Long registeredBy, String observations);
}
