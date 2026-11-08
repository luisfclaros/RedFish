package com.RedFish.RedFish.production.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

public interface FeedingRecordRepositoryPort {
    FeedingRecord save(FeedingRecord record);
    Optional<FeedingRecord> findById(Long id);
    List<FeedingRecord> findAll();
    List<FeedingRecord> findByPondId(Long pondId);
}
