package com.RedFish.RedFish.modules.orders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.orders.application.port.DispatchAvailabilityPort;
import com.RedFish.RedFish.orders.application.port.InventoryReservationPort;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.application.service.UpdateOrderStatusService;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;

class UpdateOrderStatusServiceTests {

    @Test
    void cancellationIsRejectedWhenOrderHasAnActiveDispatch() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        InventoryReservationPort inventory = mock(InventoryReservationPort.class);
        DispatchAvailabilityPort dispatches = mock(DispatchAvailabilityPort.class);
        Order order = sampleOrder();
        when(repository.findByIdForUpdate(7L)).thenReturn(Optional.of(order));
        doThrow(new IllegalStateException("orders with an active dispatch cannot be cancelled"))
                .when(dispatches).ensureNoActiveDispatch(7L);

        UpdateOrderStatusService service = new UpdateOrderStatusService(repository, inventory, dispatches);

        assertThatThrownBy(() -> service.cancel(7L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("active dispatch");

        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
        verify(inventory, never()).release(anyList());
        verify(repository, never()).save(any());
    }

    @Test
    void successfulCancellationReleasesReservedStockOnceAndSavesOrder() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        InventoryReservationPort inventory = mock(InventoryReservationPort.class);
        DispatchAvailabilityPort dispatches = mock(DispatchAvailabilityPort.class);
        Order order = sampleOrder();
        when(repository.findByIdForUpdate(7L)).thenReturn(Optional.of(order));
        when(repository.save(order)).thenReturn(order);

        UpdateOrderStatusService service = new UpdateOrderStatusService(repository, inventory, dispatches);
        Order result = service.cancel(7L);

        assertThat(result.status()).isEqualTo(OrderStatus.CANCELLED);
        verify(dispatches).ensureNoActiveDispatch(7L);
        verify(inventory).release(order.items());
        verify(repository).save(order);
    }


    @Test
    void markingOrderDispatchedIsRejectedWhenNoActiveDispatchExists() {
        OrderRepositoryPort repository = mock(OrderRepositoryPort.class);
        InventoryReservationPort inventory = mock(InventoryReservationPort.class);
        DispatchAvailabilityPort dispatches = mock(DispatchAvailabilityPort.class);
        Order order = sampleOrder();
        order.markInPreparation();
        when(repository.findByIdForUpdate(7L)).thenReturn(Optional.of(order));
        doThrow(new IllegalStateException("orders cannot be marked dispatched without an active dispatch"))
                .when(dispatches).ensureHasActiveDispatch(7L);

        UpdateOrderStatusService service = new UpdateOrderStatusService(repository, inventory, dispatches);

        assertThatThrownBy(() -> service.markDispatched(7L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("without an active dispatch");

        assertThat(order.status()).isEqualTo(OrderStatus.IN_PREPARATION);
        verify(repository, never()).save(any());
    }

    private static Order sampleOrder() {
        Customer customer = new Customer(2L, "Cliente de prueba", "3001234567",
                "Neiva", "cliente@example.com", true);
        return new Order(7L, customer, LocalDate.now(),
                List.of(new OrderItem(3L, "Tilapia roja", new BigDecimal("2.00"),
                        new BigDecimal("15000.00"))), null);
    }
}
