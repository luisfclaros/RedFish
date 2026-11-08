package com.RedFish.RedFish.production.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.RedFish.RedFish.production.application.service.ListFeedingRecordsService;
import com.RedFish.RedFish.production.application.service.RegisterFeedingRecordService;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/feeding-records")
public class FeedingRecordController {
    private final RegisterFeedingRecordService register;
    private final ListFeedingRecordsService list;

    public FeedingRecordController(RegisterFeedingRecordService register, ListFeedingRecordsService list) {
        this.register=register; this.list=list;
    }

    @PostMapping
    public ResponseEntity<FeedingRecordResponse> register(@Valid @RequestBody FeedingRecordRequest request) {
        FeedingRecord record = register.register(new FeedingRecord(null, request.pondId(), request.date(),
                request.foodType(), request.quantity(), request.registeredBy(), request.observations()));
        return ResponseEntity.created(URI.create("/api/v1/feeding-records/" + record.id()))
                .body(FeedingRecordResponse.from(record));
    }

    @GetMapping
    public List<FeedingRecordResponse> list(@RequestParam(required=false) Long pondId) {
        return list.list(pondId).stream().map(FeedingRecordResponse::from).toList();
    }
}
