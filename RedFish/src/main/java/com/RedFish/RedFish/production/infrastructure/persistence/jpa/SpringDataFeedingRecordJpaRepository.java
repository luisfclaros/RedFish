package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataFeedingRecordJpaRepository extends JpaRepository<FeedingRecordJpaEntity, Long> {
    List<FeedingRecordJpaEntity> findByPondIdOrderByDateDesc(Long pondId);
}
