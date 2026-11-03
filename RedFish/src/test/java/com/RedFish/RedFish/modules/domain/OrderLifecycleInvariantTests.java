package com.RedFish.RedFish.modules.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;

class OrderLifecycleInvariantTests {

    private Order newOrder() {
        Customer customer = new Customer(1L, "Customer", "3001234567", "Address", null, true);
        List<OrderItem> items = List.of(
            new OrderItem(10L, "Tilapia", BigDecimal.ONE, new BigDecimal("12000")));
        return new Order(null, customer, LocalDate.of(2026, 11, 3), items, null);
    }

    @Test
    void orderFollowsPendingPreparationDispatchedLifecycle() {
        Order order = newOrder();
        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
        order.markInPreparation();
        assertThat(order.status()).isEqualTo(OrderStatus.IN_PREPARATION);
        order.markDispatched();
        assertThat(order.status()).isEqualTo(OrderStatus.DISPATCHED);
    }

    @Test
    void orderCannotBeDispatchedDirectlyFromPending() {
        Order order = newOrder();
        assertThatThrownBy(order::markDispatched)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid order status transition");
        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    void cancelledOrderCannotEnterPreparation() {
        Order order = newOrder();
        order.cancel();
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
        assertThatThrownBy(order::markInPreparation)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid order status transition");
        assertThat(order.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void dispatchedOrderCannotBeCancelled() {
        Order order = newOrder();
        order.markInPreparation();
        order.markDispatched();
        assertThatThrownBy(order::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("dispatched orders cannot be cancelled");
        assertThat(order.status()).isEqualTo(OrderStatus.DISPATCHED);
    }
}
