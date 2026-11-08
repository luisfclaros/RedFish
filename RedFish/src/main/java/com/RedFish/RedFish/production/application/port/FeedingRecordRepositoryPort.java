package com.RedFish.RedFish.production.application.port;

import java.util.List;

import com.RedFish.RedFish.production.domain.model.FeedingRecord;

public interface FeedingRecordRepositoryPort {
    FeedingRecord save(FeedingRecord record);
    List<FeedingRecord> findAll();
    List<FeedingRecord> findByPondId(Long pondId);
}
