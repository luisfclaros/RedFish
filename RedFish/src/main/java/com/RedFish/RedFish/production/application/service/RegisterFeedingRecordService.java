package com.RedFish.RedFish.production.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;

@Service
public class RegisterFeedingRecordService {
    private final PondRepositoryPort ponds;
    private final FeedingRecordRepositoryPort records;

    public RegisterFeedingRecordService(PondRepositoryPort ponds, FeedingRecordRepositoryPort records) {
        this.ponds = ponds; this.records = records;
    }

    @Transactional
    public FeedingRecord register(FeedingRecord record) {
        Pond pond = ponds.findById(record.pondId())
                .orElseThrow(() -> new IllegalArgumentException("pond not found"));
        pond.ensureCanReceiveFeedingRecord();
        return records.save(record);
    }
}
