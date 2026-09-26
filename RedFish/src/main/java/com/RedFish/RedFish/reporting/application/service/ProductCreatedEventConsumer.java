package com.RedFish.RedFish.reporting.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.reporting.application.port.ProcessedProductEventPort;

@Component
public class ProductCreatedEventConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductCreatedEventConsumer.class);

	private final ProcessedProductEventPort processedEventPort;

	public ProductCreatedEventConsumer(ProcessedProductEventPort processedEventPort) {
		this.processedEventPort = processedEventPort;
	}

	@EventListener
	public void consume(ProductCreatedEvent event) {
		if (!processedEventPort.recordIfAbsent(event)) {
			LOGGER.debug("Ignoring duplicate product event {}", event.eventId());
			return;
		}
		LOGGER.info("Reporting processed product {} from event {}", event.productCode(), event.eventId());
	}
}
