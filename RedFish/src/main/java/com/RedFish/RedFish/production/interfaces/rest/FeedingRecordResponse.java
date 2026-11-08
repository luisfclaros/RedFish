package com.RedFish.RedFish.production.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

public record FeedingRecordResponse(Long id, Long pondId, Long productId, LocalDate date, String foodType,
        BigDecimal quantity, Long registeredBy, String observations) {
    public static FeedingRecordResponse from(FeedingRecord r) {
        return new FeedingRecordResponse(r.id(), r.pondId(), r.productId(), r.date(), r.foodType(),
                r.quantity(), r.registeredBy(), r.observations());
    }
}
