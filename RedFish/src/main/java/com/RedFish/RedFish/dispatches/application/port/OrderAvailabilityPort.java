package com.RedFish.RedFish.dispatches.application.port;

public interface OrderAvailabilityPort {
    void ensureOrderCanBeDispatched(Long orderId);
}
