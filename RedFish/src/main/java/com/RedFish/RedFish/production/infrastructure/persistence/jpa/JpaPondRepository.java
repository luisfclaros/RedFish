package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.domain.model.Pond;

@Repository
public class JpaPondRepository implements PondRepositoryPort {
    private final SpringDataPondJpaRepository repository;

    public JpaPondRepository(SpringDataPondJpaRepository repository) { this.repository = repository; }

    @Override public Pond save(Pond pond) { return repository.save(PondJpaEntity.fromDomain(pond)).toDomain(); }
    @Override public Optional<Pond> findById(Long id) { return repository.findById(id).map(PondJpaEntity::toDomain); }
    @Override public List<Pond> findAll() { return repository.findAll().stream().map(PondJpaEntity::toDomain).toList(); }
    @Override public boolean existsByCode(String code) { return repository.existsByCodeIgnoreCase(code); }
}
