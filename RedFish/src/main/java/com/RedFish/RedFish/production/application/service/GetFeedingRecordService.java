package com.RedFish.RedFish.production.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

@Service
public class GetFeedingRecordService {
    private final FeedingRecordRepositoryPort repository;
    public GetFeedingRecordService(FeedingRecordRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true)
    public FeedingRecord get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("feeding record not found"));
    }
}
