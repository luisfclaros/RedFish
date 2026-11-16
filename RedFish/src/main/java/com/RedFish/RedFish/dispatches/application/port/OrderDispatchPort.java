package com.RedFish.RedFish.dispatches.application.port;

public interface OrderDispatchPort {
    void markOrderDispatched(Long orderId);
}
