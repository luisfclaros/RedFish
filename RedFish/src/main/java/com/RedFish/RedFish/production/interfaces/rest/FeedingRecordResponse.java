package com.RedFish.RedFish.production.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

public record FeedingRecordResponse(Long id, Long pondId, LocalDate date, String foodType,
        BigDecimal quantity, Long registeredBy, String observations) {
    public static FeedingRecordResponse from(FeedingRecord r) {
        return new FeedingRecordResponse(r.id(), r.pondId(), r.date(), r.foodType(),
                r.quantity(), r.registeredBy(), r.observations());
    }
}
