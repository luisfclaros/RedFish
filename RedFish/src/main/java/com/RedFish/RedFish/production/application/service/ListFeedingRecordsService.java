package com.RedFish.RedFish.production.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

@Service
public class ListFeedingRecordsService {
    private final FeedingRecordRepositoryPort repository;
    public ListFeedingRecordsService(FeedingRecordRepositoryPort repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public List<FeedingRecord> list(Long pondId) {
        return pondId == null ? repository.findAll() : repository.findByPondId(pondId);
    }
}
