package com.RedFish.RedFish.production.application.port;

import java.util.List;
import java.util.Optional;

import com.RedFish.RedFish.production.domain.model.Pond;

public interface PondRepositoryPort {
    Pond save(Pond pond);
    Optional<Pond> findById(Long id);
    List<Pond> findAll();
    boolean existsByCode(String code);
}
