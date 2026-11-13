package com.RedFish.RedFish.modules.dispatches;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.application.port.OrderAvailabilityPort;
import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.dispatches.application.service.ScheduleDispatchService;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

class ScheduleDispatchServiceTests {

    @Test
    void schedulesDispatchWhenOrderHasNoExistingActiveDispatch() {
        DispatchRepositoryPort dispatches = mock(DispatchRepositoryPort.class);
        VehicleAvailabilityPort vehicles = mock(VehicleAvailabilityPort.class);
        OrderAvailabilityPort orders = mock(OrderAvailabilityPort.class);
        when(dispatches.existsActiveByOrderId(15L)).thenReturn(false);
        when(dispatches.save(any(Dispatch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleDispatchService service = new ScheduleDispatchService(dispatches, vehicles, orders);
        Dispatch result = service.schedule(15L, 7L, LocalDate.of(2026, 11, 12), "Bodega principal");

        assertThat(result.orderId()).isEqualTo(15L);
        assertThat(result.vehicleId()).isEqualTo(7L);
        verify(orders).ensureOrderCanBeDispatched(15L);
        verify(vehicles).ensureVehicleCanBeAssigned(7L);
        verify(dispatches).save(any(Dispatch.class));
    }

    @Test
    void rejectsSecondActiveDispatchForSameOrder() {
        DispatchRepositoryPort dispatches = mock(DispatchRepositoryPort.class);
        VehicleAvailabilityPort vehicles = mock(VehicleAvailabilityPort.class);
        OrderAvailabilityPort orders = mock(OrderAvailabilityPort.class);
        when(dispatches.existsActiveByOrderId(15L)).thenReturn(true);

        ScheduleDispatchService service = new ScheduleDispatchService(dispatches, vehicles, orders);

        assertThatThrownBy(() -> service.schedule(15L, 8L, LocalDate.now(), "Bodega"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("order already has an active dispatch");

        verify(orders).ensureOrderCanBeDispatched(15L);
        verify(vehicles, never()).ensureVehicleCanBeAssigned(8L);
        verify(dispatches, never()).save(any(Dispatch.class));
    }

    @Test
    void allowsReschedulingAfterPreviousDispatchWasCancelled() {
        DispatchRepositoryPort dispatches = mock(DispatchRepositoryPort.class);
        VehicleAvailabilityPort vehicles = mock(VehicleAvailabilityPort.class);
        OrderAvailabilityPort orders = mock(OrderAvailabilityPort.class);
        when(dispatches.existsActiveByOrderId(15L)).thenReturn(false);
        when(dispatches.save(any(Dispatch.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ScheduleDispatchService service = new ScheduleDispatchService(dispatches, vehicles, orders);
        Dispatch result = service.schedule(15L, 9L, LocalDate.now(), "Bodega");

        assertThat(result.status().name()).isEqualTo("SCHEDULED");
        verify(dispatches).save(any(Dispatch.class));
    }
}
