package com.RedFish.RedFish.production.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.domain.model.Pond;

@Service
public class CreatePondService {
    private final PondRepositoryPort repository;
    public CreatePondService(PondRepositoryPort repository) { this.repository = repository; }

    @Transactional
    public Pond create(Pond pond) {
        if (repository.existsByCode(pond.code())) throw new IllegalArgumentException("pond code already exists");
        return repository.save(pond);
    }
}
