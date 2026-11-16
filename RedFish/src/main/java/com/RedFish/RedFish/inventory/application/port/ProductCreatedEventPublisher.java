package com.RedFish.RedFish.inventory.application.port;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;

@FunctionalInterface
public interface ProductCreatedEventPublisher {

	void publish(ProductCreatedEvent event);
}
