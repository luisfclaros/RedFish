package com.RedFish.RedFish.orders.interfaces.rest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;

public record OrderResponse(Long id, Long customerId, LocalDate date, List<OrderLineResponse> items,
        BigDecimal total, OrderStatus status, String observations) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.id(), order.customerId(), order.date(),
                order.items().stream().map(i -> new OrderLineResponse(i.productId(), i.productName(),
                        i.quantity(), i.unitPrice(), i.subtotal())).toList(),
                order.total(), order.status(), order.observations());
    }
    public record OrderLineResponse(Long productId, String productName, BigDecimal quantity,
            BigDecimal unitPrice, BigDecimal subtotal) {}
}
