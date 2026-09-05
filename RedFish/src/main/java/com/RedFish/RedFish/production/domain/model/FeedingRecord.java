package com.RedFish.RedFish.production.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requirePositive;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FeedingRecord(Long id, Long pondId, LocalDate date, String foodType, BigDecimal quantity,
		Long registeredBy, String observations) {

	public FeedingRecord {
		requireNonNull(pondId, "pond id");
		requireNonNull(date, "feeding date");
		foodType = requireText(foodType, "food type");
		quantity = requirePositive(quantity, "feeding quantity");
		requireNonNull(registeredBy, "registered by");
	}

	public static FeedingRecord registerFor(Pond pond, LocalDate date, String foodType, BigDecimal quantity,
			Long registeredBy, String observations) {
		requireNonNull(pond, "pond").ensureCanReceiveFeedingRecord();
		return new FeedingRecord(null, pond.id(), date, foodType, quantity, registeredBy, observations);
	}
}
