package com.RedFish.RedFish.modules.production.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.RedFish.RedFish.production.application.port.FeedingInventoryPort;
import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.application.service.RegisterFeedingRecordService;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import com.RedFish.RedFish.production.domain.model.Pond;

class RegisterFeedingRecordServiceTests {

    private PondRepositoryPort ponds;
    private FeedingRecordRepositoryPort records;
    private FeedingInventoryPort inventory;
    private RegisterFeedingRecordService service;
    private Pond activePond;

    @BeforeEach
    void setUp() {
        ponds = mock(PondRepositoryPort.class);
        records = mock(FeedingRecordRepositoryPort.class);
        inventory = mock(FeedingInventoryPort.class);
        service = new RegisterFeedingRecordService(ponds, records, inventory);
        activePond = new Pond(1L, "P-01", "Estanque 1", "Zona A",
                new BigDecimal("1000"), true);
        when(ponds.findById(1L)).thenReturn(Optional.of(activePond));
    }

    @Test
    void consumesFoodBeforeSavingFeedingRecord() {
        FeedingRecord record = recordWithProduct(20L);
        when(records.save(record)).thenReturn(record);

        FeedingRecord saved = service.register(record);

        assertThat(saved).isSameAs(record);
        InOrder order = inOrder(inventory, records);
        order.verify(inventory).registerFoodConsumption(20L, new BigDecimal("4.5"), 7L);
        order.verify(records).save(record);
    }

    @Test
    void doesNotSaveFeedingRecordWhenFoodConsumptionFails() {
        FeedingRecord record = recordWithProduct(20L);
        org.mockito.Mockito.doThrow(new IllegalStateException("insufficient inventory"))
                .when(inventory).registerFoodConsumption(20L, new BigDecimal("4.5"), 7L);

        assertThatThrownBy(() -> service.register(record))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("insufficient inventory");

        verify(records, never()).save(record);
    }

    @Test
    void inactivePondDoesNotConsumeFoodOrSaveRecord() {
        Pond inactive = new Pond(1L, "P-01", "Estanque 1", "Zona A",
                new BigDecimal("1000"), false);
        when(ponds.findById(1L)).thenReturn(Optional.of(inactive));
        FeedingRecord record = recordWithProduct(20L);

        assertThatThrownBy(() -> service.register(record))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("inactive ponds");

        verify(inventory, never()).registerFoodConsumption(20L, new BigDecimal("4.5"), 7L);
        verify(records, never()).save(record);
    }

    @Test
    void missingPondDoesNotConsumeFoodOrSaveRecord() {
        when(ponds.findById(1L)).thenReturn(Optional.empty());
        FeedingRecord record = recordWithProduct(20L);

        assertThatThrownBy(() -> service.register(record))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("pond not found");

        verify(inventory, never()).registerFoodConsumption(20L, new BigDecimal("4.5"), 7L);
        verify(records, never()).save(record);
    }

    @Test
    void legacyFeedingRecordWithoutProductDoesNotChangeInventory() {
        FeedingRecord record = new FeedingRecord(null, 1L, LocalDate.of(2026, 11, 12),
                "Alimento", new BigDecimal("4.5"), 7L, "Registro histórico");
        when(records.save(record)).thenReturn(record);

        assertThat(service.register(record)).isSameAs(record);

        verify(inventory, never()).registerFoodConsumption(
                org.mockito.ArgumentMatchers.anyLong(),
                org.mockito.ArgumentMatchers.any(BigDecimal.class),
                org.mockito.ArgumentMatchers.anyLong());
        verify(records).save(record);
    }

    private FeedingRecord recordWithProduct(Long productId) {
        return new FeedingRecord(null, 1L, productId, LocalDate.of(2026, 11, 12),
                "Alimento", new BigDecimal("4.5"), 7L, "Ración de la mañana");
    }
}
