package com.RedFish.RedFish.orders.application.port;

/** Prevents cancelling an order while a non-cancelled dispatch is still assigned to it. */
public interface DispatchAvailabilityPort {
    void ensureNoActiveDispatch(Long orderId);

    /** Prevents marking an order as dispatched unless a real dispatch exists. */
    default void ensureHasActiveDispatch(Long orderId) {}
}
