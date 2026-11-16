package com.RedFish.RedFish.inventory.application.event;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.Instant;
import java.util.UUID;

import com.RedFish.RedFish.inventory.domain.model.Product;

public record ProductCreatedEvent(UUID eventId, Long productId, String productCode, Instant occurredAt) {

	public ProductCreatedEvent {
		requireNonNull(eventId, "event id");
		requireNonNull(productId, "product id");
		requireText(productCode, "product code");
		requireNonNull(occurredAt, "event occurrence time");
	}

	public static ProductCreatedEvent from(Product product) {
		Product createdProduct = requireNonNull(product, "created product");
		return new ProductCreatedEvent(UUID.randomUUID(), createdProduct.id(), createdProduct.code(), Instant.now());
	}
}
