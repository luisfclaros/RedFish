package com.RedFish.RedFish.orders.application.port;

import java.util.List;
import com.RedFish.RedFish.orders.domain.model.OrderItem;

public interface InventoryReservationPort {
    void reserve(List<OrderItem> items);
    default void release(List<OrderItem> items) {}
}
