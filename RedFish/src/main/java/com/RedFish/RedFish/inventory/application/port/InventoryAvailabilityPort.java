package com.RedFish.RedFish.inventory.application.port;

import java.math.BigDecimal;

public interface InventoryAvailabilityPort {

	boolean hasAvailableStock(Long productId, BigDecimal quantity);
}
