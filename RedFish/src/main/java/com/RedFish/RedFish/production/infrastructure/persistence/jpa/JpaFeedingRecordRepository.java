package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

@Repository
public class JpaFeedingRecordRepository implements FeedingRecordRepositoryPort {
    private final SpringDataFeedingRecordJpaRepository repository;

    public JpaFeedingRecordRepository(SpringDataFeedingRecordJpaRepository repository) { this.repository = repository; }

    @Override public FeedingRecord save(FeedingRecord record) {
        return repository.save(FeedingRecordJpaEntity.fromDomain(record)).toDomain();
    }
    @Override public java.util.Optional<FeedingRecord> findById(Long id) {
        return repository.findById(id).map(FeedingRecordJpaEntity::toDomain);
    }
    @Override public List<FeedingRecord> findAll() {
        return repository.findAllByOrderByDateDesc().stream().map(FeedingRecordJpaEntity::toDomain).toList();
    }
    @Override public List<FeedingRecord> findByPondId(Long pondId) {
        return repository.findByPondIdOrderByDateDesc(pondId).stream().map(FeedingRecordJpaEntity::toDomain).toList();
    }
}
