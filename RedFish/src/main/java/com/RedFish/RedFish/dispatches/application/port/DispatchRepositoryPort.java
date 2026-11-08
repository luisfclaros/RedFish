package com.RedFish.RedFish.dispatches.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

public interface DispatchRepositoryPort {
    Dispatch save(Dispatch dispatch);
    Optional<Dispatch> findById(Long id);
    default List<Dispatch> findAll() { return List.of(); }
}
