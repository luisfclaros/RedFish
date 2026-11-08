package com.RedFish.RedFish.dispatches.application.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

@Service
public class UpdateDispatchStatusService {
    private final DispatchRepositoryPort repository;
    public UpdateDispatchStatusService(DispatchRepositoryPort repository) { this.repository=repository; }

    @Transactional
    public Dispatch markInTransit(Long id) {
        Dispatch dispatch = get(id);
        dispatch.markInTransit();
        return repository.save(dispatch);
    }

    @Transactional
    public Dispatch markDelivered(Long id, LocalDate deliveryDate, String receivedBy) {
        Dispatch dispatch = get(id);
        dispatch.markDelivered(deliveryDate, receivedBy);
        return repository.save(dispatch);
    }

    @Transactional
    public Dispatch cancel(Long id) {
        Dispatch dispatch = get(id);
        dispatch.cancel();
        return repository.save(dispatch);
    }

    private Dispatch get(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("dispatch not found"));
    }
}
