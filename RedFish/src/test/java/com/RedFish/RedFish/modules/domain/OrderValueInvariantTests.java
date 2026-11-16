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

class OrderValueInvariantTests {

    private Customer activeCustomer() {
        return new Customer(1L, "Customer", "3001234567", "Address", null, true);
    }

    @Test
    void orderTotalIsTheSumOfAllLineSubtotals() {
        List<OrderItem> items = List.of(
            new OrderItem(10L, "Tilapia", new BigDecimal("2"), new BigDecimal("12000")),
            new OrderItem(11L, "Feed", new BigDecimal("1.5"), new BigDecimal("8000"))
        );

        Order order = new Order(null, activeCustomer(), LocalDate.of(2026, 11, 3), items, null);

        assertThat(order.total()).isEqualByComparingTo("36000");
        assertThat(items.get(0).subtotal()).isEqualByComparingTo("24000");
        assertThat(items.get(1).subtotal()).isEqualByComparingTo("12000.0");
    }

    @Test
    void orderItemRejectsZeroOrNegativeQuantity() {
        assertThatThrownBy(() -> new OrderItem(10L, "Tilapia", BigDecimal.ZERO, new BigDecimal("12000")))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new OrderItem(10L, "Tilapia", new BigDecimal("-1"), new BigDecimal("12000")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderItemRejectsZeroOrNegativeUnitPrice() {
        assertThatThrownBy(() -> new OrderItem(10L, "Tilapia", BigDecimal.ONE, BigDecimal.ZERO))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new OrderItem(10L, "Tilapia", BigDecimal.ONE, new BigDecimal("-1")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderRequiresAtLeastOneDetail() {
        assertThatThrownBy(() -> new Order(null, activeCustomer(), LocalDate.of(2026, 11, 3), List.of(), null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("at least one item");
    }
}
