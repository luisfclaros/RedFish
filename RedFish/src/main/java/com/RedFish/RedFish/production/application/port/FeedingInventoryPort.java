package com.RedFish.RedFish.production.application.port;

import java.math.BigDecimal;

public interface FeedingInventoryPort {

	void registerFoodConsumption(Long productId, BigDecimal quantity, Long registeredBy);
}
