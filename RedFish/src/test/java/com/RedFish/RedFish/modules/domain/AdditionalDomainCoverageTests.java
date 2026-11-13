package com.RedFish.RedFish.modules.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;

class AdditionalDomainCoverageTests {

    @Test
    void dispatchFollowsValidLifecycleAndRejectsInvalidTransitions() {
        Dispatch dispatch = new Dispatch(1L, 2L, 3L, LocalDate.of(2026, 11, 5), "Bodega");

        assertThat(dispatch.id()).isEqualTo(1L);
        assertThat(dispatch.orderId()).isEqualTo(2L);
        assertThat(dispatch.vehicleId()).isEqualTo(3L);
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.SCHEDULED);
        assertThatThrownBy(() -> dispatch.markDelivered(LocalDate.now(), "Ana"))
            .isInstanceOf(IllegalStateException.class);

        dispatch.markInTransit();
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.IN_TRANSIT);
        dispatch.markDelivered(LocalDate.now(), "Ana");
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.DELIVERED);
        assertThatThrownBy(dispatch::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid dispatch status transition");
    }

    @Test
    void dispatchCanBeCancelledOnlyBeforeItEntersTransit() {
        Dispatch scheduled = new Dispatch(1L, 2L, 3L, LocalDate.now(), "Bodega");
        scheduled.cancel();
        assertThat(scheduled.status()).isEqualTo(DispatchStatus.CANCELLED);
        assertThatThrownBy(scheduled::markInTransit).isInstanceOf(IllegalStateException.class);

        Dispatch inTransit = new Dispatch(2L, 3L, 4L, LocalDate.now(), "Cliente");
        inTransit.markInTransit();
        assertThatThrownBy(inTransit::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid dispatch status transition");
        assertThat(inTransit.status()).isEqualTo(DispatchStatus.IN_TRANSIT);
    }

    @Test
    void dispatchValidatesRequiredFields() {
        assertThatThrownBy(() -> new Dispatch(1L, 2L, 3L, null, "Bodega"))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("dispatch date");
        assertThatThrownBy(() -> new Dispatch(1L, 2L, 3L, LocalDate.now(), " "))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("delivery address");
    }

    @Test
    void inventoryItemSupportsIncreaseDecreaseAndMinimumStockChecks() {
        InventoryItem item = new InventoryItem(1L, 2L, new BigDecimal("5"), new BigDecimal("4"));

        assertThat(item.belowMinimumStock()).isFalse();
        item.decrease(new BigDecimal("2"));
        assertThat(item.quantity()).isEqualByComparingTo("3");
        assertThat(item.belowMinimumStock()).isTrue();
        item.increase(new BigDecimal("3"));
        assertThat(item.quantity()).isEqualByComparingTo("6");
        assertThat(item.id()).isEqualTo(1L);
        assertThat(item.productId()).isEqualTo(2L);
        assertThat(item.minimumStock()).isEqualByComparingTo("4");
        assertThatThrownBy(() -> item.increase(BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> item.decrease(BigDecimal.ZERO)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void inventoryMovementRequiresPositiveQuantityAndRequiredMetadata() {
        LocalDateTime date = LocalDateTime.of(2026, 11, 5, 9, 0);
        InventoryMovement movement = new InventoryMovement(1L, 2L, MovementType.ENTRY,
            new BigDecimal("4"), date, 3L, "Recepción");

        assertThat(movement.id()).isEqualTo(1L);
        assertThat(movement.inventoryId()).isEqualTo(2L);
        assertThat(movement.type()).isEqualTo(MovementType.ENTRY);
        assertThat(movement.quantity()).isEqualByComparingTo("4");
        assertThat(movement.date()).isEqualTo(date);
        assertThat(movement.registeredBy()).isEqualTo(3L);
        assertThat(movement.observations()).isEqualTo("Recepción");

        assertThatThrownBy(() -> new InventoryMovement(1L, 2L, MovementType.EXIT,
            BigDecimal.ZERO, date, 3L, null)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new InventoryMovement(1L, null, MovementType.EXIT,
            BigDecimal.ONE, date, 3L, null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void productAllowsUseOnlyWhenActiveAndValidatesAttributes() {
        Product product = new Product(1L, "AL-01", "Alimento", "kg", ProductType.INPUT, true);
        product.ensureCanBeUsed();

        assertThat(product.id()).isEqualTo(1L);
        assertThat(product.code()).isEqualTo("AL-01");
        assertThat(product.name()).isEqualTo("Alimento");
        assertThat(product.unitOfMeasure()).isEqualTo("kg");
        assertThat(product.type()).isEqualTo(ProductType.INPUT);
        assertThat(product.active()).isTrue();

        Product inactive = new Product(2L, "PR-02", "Tilapia", "kg", ProductType.PRODUCT, false);
        assertThatThrownBy(inactive::ensureCanBeUsed)
            .isInstanceOf(IllegalStateException.class).hasMessageContaining("inactive products");
        assertThatThrownBy(() -> new Product(3L, " ", "Alimento", "kg", ProductType.INPUT, true))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("product code");
    }

    @Test
    void orderTransitionsAndRejectsEmptyOrdersOrInactiveCustomers() {
        Customer customer = new Customer(1L, "Cliente", "3001234567", "Dirección", null, true);
        Order order = new Order(1L, customer, LocalDate.now(),
            List.of(new OrderItem(10L, "Tilapia", new BigDecimal("2"), new BigDecimal("5"))), "Urgente");

        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.observations()).isEqualTo("Urgente");
        assertThat(order.items()).hasSize(1);
        order.markInPreparation();
        assertThat(order.status()).isEqualTo(OrderStatus.IN_PREPARATION);
        order.markDispatched();
        assertThat(order.status()).isEqualTo(OrderStatus.DISPATCHED);
        assertThatThrownBy(order::cancel).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(order::markInPreparation).isInstanceOf(IllegalStateException.class);

        assertThatThrownBy(() -> new Order(2L, customer, LocalDate.now(), List.of(), null))
            .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("at least one item");
        Customer inactive = new Customer(2L, "Inactivo", "3000000000", "Dirección", null, false);
        assertThatThrownBy(() -> new Order(3L, inactive, LocalDate.now(),
            List.of(new OrderItem(10L, "Tilapia", BigDecimal.ONE, BigDecimal.ONE)), null))
            .isInstanceOf(IllegalStateException.class).hasMessageContaining("inactive customers");
    }

    @Test
    void orderItemCalculatesSubtotalAndValidatesPositiveValues() {
        OrderItem item = new OrderItem(1L, "Tilapia", new BigDecimal("2.5"), new BigDecimal("12000"));
        assertThat(item.subtotal()).isEqualByComparingTo("30000");
        assertThatThrownBy(() -> new OrderItem(1L, "Tilapia", BigDecimal.ZERO, BigDecimal.ONE))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new OrderItem(1L, " ", BigDecimal.ONE, BigDecimal.ONE))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pondAndFeedingRecordEnforceActivePondAndValidFeedingData() {
        Pond pond = new Pond(1L, "P-01", "Estanque 1", "Zona norte", new BigDecimal("100"), true);
        assertThat(pond.id()).isEqualTo(1L);
        assertThat(pond.code()).isEqualTo("P-01");
        assertThat(pond.name()).isEqualTo("Estanque 1");
        assertThat(pond.location()).isEqualTo("Zona norte");
        assertThat(pond.capacity()).isEqualByComparingTo("100");
        assertThat(pond.active()).isTrue();

        FeedingRecord record = FeedingRecord.registerFor(pond, LocalDate.now(), "Concentrado",
            new BigDecimal("8"), 4L, "Turno mañana");
        assertThat(record.pondId()).isEqualTo(1L);
        assertThat(record.foodType()).isEqualTo("Concentrado");
        assertThat(record.quantity()).isEqualByComparingTo("8");
        assertThat(record.registeredBy()).isEqualTo(4L);
        assertThat(record.observations()).isEqualTo("Turno mañana");

        Pond inactive = new Pond(2L, "P-02", "Estanque 2", "Zona sur", BigDecimal.TEN, false);
        assertThatThrownBy(inactive::ensureCanReceiveFeedingRecord).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> FeedingRecord.registerFor(inactive, LocalDate.now(), "Concentrado",
            BigDecimal.ONE, 4L, null)).isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> new Pond(3L, " ", "Estanque", "Zona", BigDecimal.ONE, true))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
