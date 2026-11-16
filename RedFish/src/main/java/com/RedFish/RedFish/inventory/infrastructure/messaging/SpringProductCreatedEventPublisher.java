package com.RedFish.RedFish.inventory.infrastructure.messaging;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.inventory.application.port.ProductCreatedEventPublisher;

@Component
public class SpringProductCreatedEventPublisher implements ProductCreatedEventPublisher {

	private final ApplicationEventPublisher eventPublisher;

	public SpringProductCreatedEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void publish(ProductCreatedEvent event) {
		eventPublisher.publishEvent(event);
	}
}
