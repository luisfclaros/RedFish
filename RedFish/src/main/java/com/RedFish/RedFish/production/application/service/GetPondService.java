package com.RedFish.RedFish.production.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.domain.model.Pond;

@Service
public class GetPondService {
    private final PondRepositoryPort repository;
    public GetPondService(PondRepositoryPort repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public Pond get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("pond not found"));
    }
}
