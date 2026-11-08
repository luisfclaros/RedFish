package com.RedFish.RedFish.production.interfaces.rest;

import java.math.BigDecimal;
import com.RedFish.RedFish.production.domain.model.Pond;

public record PondResponse(Long id, String code, String name, String location, BigDecimal capacity, boolean active) {
    public static PondResponse from(Pond p) {
        return new PondResponse(p.id(), p.code(), p.name(), p.location(), p.capacity(), p.active());
    }
}
