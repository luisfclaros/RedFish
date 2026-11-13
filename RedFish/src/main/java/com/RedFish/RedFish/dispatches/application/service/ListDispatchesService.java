package com.RedFish.RedFish.dispatches.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

@Service
public class ListDispatchesService {
    private final DispatchRepositoryPort repository;
    public ListDispatchesService(DispatchRepositoryPort repository) { this.repository=repository; }
    @Transactional(readOnly=true) public List<Dispatch> list() { return repository.findAll(); }
}
