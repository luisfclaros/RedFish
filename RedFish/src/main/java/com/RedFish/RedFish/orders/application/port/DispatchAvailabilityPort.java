package com.RedFish.RedFish.orders.application.port;

/** Prevents cancelling an order while a non-cancelled dispatch is still assigned to it. */
public interface DispatchAvailabilityPort {
    void ensureNoActiveDispatch(Long orderId);
}
