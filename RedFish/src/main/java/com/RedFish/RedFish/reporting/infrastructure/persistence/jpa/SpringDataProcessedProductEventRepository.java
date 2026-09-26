package com.RedFish.RedFish.reporting.infrastructure.persistence.jpa;

import java.time.Instant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataProcessedProductEventRepository
		extends JpaRepository<ProcessedProductEventJpaEntity, String> {

	@Modifying
	@Query(value = """
			INSERT IGNORE INTO eventos_producto_procesados
				(event_id, product_id, product_code, occurred_at, processed_at)
			VALUES
				(:eventId, :productId, :productCode, :occurredAt, :processedAt)
			""", nativeQuery = true)
	int insertIfAbsent(@Param("eventId") String eventId, @Param("productId") Long productId,
			@Param("productCode") String productCode, @Param("occurredAt") Instant occurredAt,
			@Param("processedAt") Instant processedAt);
}
