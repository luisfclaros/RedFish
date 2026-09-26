package com.RedFish.RedFish.reporting.infrastructure.persistence.jpa;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventos_producto_procesados")
public class ProcessedProductEventJpaEntity {

	@Id
	@Column(name = "event_id", nullable = false, length = 36)
	private String eventId;

	@Column(name = "product_id", nullable = false)
	private Long productId;

	@Column(name = "product_code", nullable = false, length = 20)
	private String productCode;

	@Column(name = "occurred_at", nullable = false)
	private Instant occurredAt;

	@Column(name = "processed_at", nullable = false)
	private Instant processedAt;

	protected ProcessedProductEventJpaEntity() {
	}

}
