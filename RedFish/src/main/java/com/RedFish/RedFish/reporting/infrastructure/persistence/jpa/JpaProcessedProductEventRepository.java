package com.RedFish.RedFish.reporting.infrastructure.persistence.jpa;

import java.time.Instant;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.reporting.application.port.ProcessedProductEventPort;

@Repository
public class JpaProcessedProductEventRepository implements ProcessedProductEventPort {

	private final SpringDataProcessedProductEventRepository repository;

	public JpaProcessedProductEventRepository(SpringDataProcessedProductEventRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public boolean recordIfAbsent(ProductCreatedEvent event) {
		return repository.insertIfAbsent(event.eventId().toString(), event.productId(), event.productCode(),
				event.occurredAt(), Instant.now()) == 1;
	}
}
