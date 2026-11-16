package com.RedFish.RedFish.modules.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

class ProductionDispatchVehicleInvariantTests {

    @Test
    void dispatchFollowsScheduledInTransitDeliveredLifecycle() {
        Dispatch dispatch = new Dispatch(1L, 10L, 20L, LocalDate.of(2026, 11, 3), "Farm delivery point");

        assertThat(dispatch.status()).isEqualTo(DispatchStatus.SCHEDULED);

        dispatch.markInTransit();
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.IN_TRANSIT);

        dispatch.markDelivered(LocalDate.of(2026, 11, 4), "Store manager");
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.DELIVERED);
    }

    @Test
    void dispatchCannotBeDeliveredBeforeGoingInTransit() {
        Dispatch dispatch = new Dispatch(1L, 10L, 20L, LocalDate.of(2026, 11, 3), "Farm delivery point");

        assertThatThrownBy(() -> dispatch.markDelivered(LocalDate.of(2026, 11, 4), "Store manager"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid dispatch status transition");
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.SCHEDULED);
    }

    @Test
    void dispatchInTransitCannotBeCancelled() {
        Dispatch dispatch = new Dispatch(1L, 10L, 20L, LocalDate.of(2026, 11, 3), "Farm delivery point");
        dispatch.markInTransit();

        assertThatThrownBy(dispatch::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid dispatch status transition");
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.IN_TRANSIT);
    }

    @Test
    void deliveredDispatchCannotBeCancelled() {
        Dispatch dispatch = new Dispatch(1L, 10L, 20L, LocalDate.of(2026, 11, 3), "Farm delivery point");
        dispatch.markInTransit();
        dispatch.markDelivered(LocalDate.of(2026, 11, 4), "Store manager");

        assertThatThrownBy(dispatch::cancel)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("invalid dispatch status transition");
        assertThat(dispatch.status()).isEqualTo(DispatchStatus.DELIVERED);
    }

    @Test
    void inactiveVehicleCannotBeAssigned() {
        Vehicle vehicle = new Vehicle(20L, "ABC123", "Isuzu", "NPR", new BigDecimal("3000"), false, null);

        assertThatThrownBy(vehicle::ensureCanBeAssigned)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("inactive vehicles");
    }

    @Test
    void feedingRecordRequiresPositiveQuantity() {
        Pond pond = new Pond(1L, "P-01", "Pond 1", "Zone A", new BigDecimal("100"), true);

        assertThatThrownBy(() -> FeedingRecord.registerFor(pond, LocalDate.of(2026, 11, 3),
                "Pellet feed", BigDecimal.ZERO, 7L, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("feeding quantity");
    }
}
