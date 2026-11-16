package com.RedFish.RedFish.modules.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;

class ProductionAndOrderDomainInvariantTests {

    @Test
    void inactivePondCannotReceiveFeedingRecord() {
        Pond pond = new Pond(1L, "P-01", "Pond 1", "Zone A", new BigDecimal("100"), false);

        assertThatThrownBy(() -> FeedingRecord.registerFor(pond, LocalDate.of(2026, 11, 3),
                "Pellet feed", BigDecimal.ONE, 7L, null))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("inactive ponds");
    }

    @Test
    void inactiveCustomerCannotPlaceOrder() {
        Customer customer = new Customer(1L, "Customer", "3001234567", "Address", null, false);
        List<OrderItem> items = List.of(new OrderItem(10L, "Tilapia", BigDecimal.ONE, new BigDecimal("12000")));

        assertThatThrownBy(() -> new Order(null, customer, LocalDate.of(2026, 11, 3), items, null))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("inactive customers");
    }

    @Test
    void orderKeepsDefensiveCopyOfProvidedItems() {
        Customer customer = new Customer(1L, "Customer", "3001234567", "Address", null, true);
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(10L, "Tilapia", BigDecimal.ONE, new BigDecimal("12000")));

        Order order = new Order(null, customer, LocalDate.of(2026, 11, 3), items, null);
        items.clear();

        assertThat(order.items()).hasSize(1);
        assertThat(order.total()).isEqualByComparingTo("12000");
    }

    @Test
    void orderItemsCannotBeModifiedThroughGetter() {
        Customer customer = new Customer(1L, "Customer", "3001234567", "Address", null, true);
        OrderItem item = new OrderItem(10L, "Tilapia", BigDecimal.ONE, new BigDecimal("12000"));
        Order order = new Order(null, customer, LocalDate.of(2026, 11, 3), List.of(item), null);

        assertThatThrownBy(() -> order.items().add(item))
            .isInstanceOf(UnsupportedOperationException.class);
    }
}
