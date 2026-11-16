package com.RedFish.RedFish.modules.reporting.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.reporting.application.port.ProcessedProductEventPort;
import com.RedFish.RedFish.reporting.application.service.ProductCreatedEventConsumer;

class ProductCreatedEventConsumerTests {

	@Test
	void processesTheSameEventOnlyOnce() {
		InMemoryProcessedProductEventPort processedEvents = new InMemoryProcessedProductEventPort();
		ProductCreatedEventConsumer consumer = new ProductCreatedEventConsumer(processedEvents);
		ProductCreatedEvent event = new ProductCreatedEvent(UUID.randomUUID(), 1L, "PROD-001", Instant.now());

		consumer.consume(event);
		consumer.consume(event);

		assertThat(processedEvents.recordedEventIds).containsExactly(event.eventId());
	}

	private static final class InMemoryProcessedProductEventPort implements ProcessedProductEventPort {

		private final Set<UUID> recordedEventIds = new HashSet<>();

		@Override
		public boolean recordIfAbsent(ProductCreatedEvent event) {
			return recordedEventIds.add(event.eventId());
		}
	}
}
