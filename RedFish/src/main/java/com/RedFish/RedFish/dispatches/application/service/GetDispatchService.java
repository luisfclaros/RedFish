package com.RedFish.RedFish.dispatches.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

@Service
public class GetDispatchService {
    private final DispatchRepositoryPort repository;
    public GetDispatchService(DispatchRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true)
    public Dispatch get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("dispatch not found"));
    }
}
