package com.RedFish.RedFish.production.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.domain.model.Pond;

@Service
public class ListPondsService {
    private final PondRepositoryPort repository;
    public ListPondsService(PondRepositoryPort repository) { this.repository = repository; }
    @Transactional(readOnly = true) public List<Pond> list() { return repository.findAll(); }
}
