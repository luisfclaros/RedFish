package com.RedFish.RedFish.dispatches.application.port;

import java.util.Optional;

import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

public interface DispatchRepositoryPort {

	Dispatch save(Dispatch dispatch);

	Optional<Dispatch> findById(Long id);
}
