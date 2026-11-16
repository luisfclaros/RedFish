package com.RedFish.RedFish.modules.inventory.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import com.RedFish.RedFish.inventory.application.port.InventoryMovementRepositoryPort;
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;
import com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.JpaInventoryMovementAdapter;

class InventoryMovementAdapterTests {

    private InventoryRepositoryPort inventory;
    private InventoryMovementRepositoryPort movements;
    private JpaInventoryMovementAdapter adapter;

    @BeforeEach
    void setUp() {
        inventory = mock(InventoryRepositoryPort.class);
        movements = mock(InventoryMovementRepositoryPort.class);
        adapter = new JpaInventoryMovementAdapter(inventory, movements);
    }

    @Test
    void entryLocksStockUpdatesQuantityAndRecordsMovement() {
        InventoryItem item = new InventoryItem(11L, 7L, new BigDecimal("5"), BigDecimal.ZERO);
        when(inventory.findByProductIdForUpdate(7L)).thenReturn(Optional.of(item));

        adapter.registerEntry(7L, new BigDecimal("2.5"), 3L, "Reposición");

        assertThat(item.quantity()).isEqualByComparingTo("7.5");
        InOrder order = inOrder(inventory, movements);
        order.verify(inventory).findByProductIdForUpdate(7L);
        order.verify(inventory).save(item);
        order.verify(movements).save(any(InventoryMovement.class));
    }

    @Test
    void exitLocksStockAndRecordsAnExitMovement() {
        InventoryItem item = new InventoryItem(11L, 7L, new BigDecimal("5"), BigDecimal.ZERO);
        when(inventory.findByProductIdForUpdate(7L)).thenReturn(Optional.of(item));

        adapter.registerExit(7L, new BigDecimal("2"), 3L, "Consumo");

        assertThat(item.quantity()).isEqualByComparingTo("3");
        verify(inventory).findByProductIdForUpdate(7L);
        verify(inventory).save(item);
        verify(movements).save(org.mockito.ArgumentMatchers.argThat(movement ->
                movement.type() == MovementType.EXIT
                        && movement.quantity().compareTo(new BigDecimal("2")) == 0
                        && movement.inventoryId().equals(11L)));
    }

    @Test
    void rejectsExitWithoutChangingOrRecordingWhenStockIsInsufficient() {
        InventoryItem item = new InventoryItem(11L, 7L, new BigDecimal("1"), BigDecimal.ZERO);
        when(inventory.findByProductIdForUpdate(7L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> adapter.registerExit(7L, new BigDecimal("2"), 3L, "Consumo"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cannot be negative");

        assertThat(item.quantity()).isEqualByComparingTo("1");
        verify(inventory, never()).save(any(InventoryItem.class));
        verify(movements, never()).save(any(InventoryMovement.class));
    }

    @Test
    void rejectsMovementWhenProductHasNoInventoryStock() {
        when(inventory.findByProductIdForUpdate(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adapter.registerEntry(7L, BigDecimal.ONE, 3L, "Reposición"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("stock not registered");

        verify(inventory, never()).save(any(InventoryItem.class));
        verify(movements, never()).save(any(InventoryMovement.class));
    }
}
