package com.RedFish.RedFish.dispatches.application.port;

import java.util.List;
import java.util.Optional;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

public interface DispatchRepositoryPort {
    Dispatch save(Dispatch dispatch);
    Optional<Dispatch> findById(Long id);
    default List<Dispatch> findAll() { return List.of(); }
    /** Returns true when an order already has a dispatch that has not been cancelled. */
    default boolean existsActiveByOrderId(Long orderId) { return false; }
    /** Returns true when a vehicle is assigned to a scheduled or in-transit dispatch. */
    default boolean existsActiveByVehicleId(Long vehicleId) { return false; }
}
