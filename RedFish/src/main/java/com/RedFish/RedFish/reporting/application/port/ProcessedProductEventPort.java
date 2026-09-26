package com.RedFish.RedFish.reporting.application.port;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;

public interface ProcessedProductEventPort {

	boolean recordIfAbsent(ProductCreatedEvent event);
}
