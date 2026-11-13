package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.RedFish.RedFish.inventory.domain.model.InventoryMovement;
import com.RedFish.RedFish.inventory.domain.model.MovementType;
import jakarta.persistence.*;

@Entity
@Table(name="movimientos_inventario")
public class InventoryMovementJpaEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private Long inventoryId;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=10) private MovementType type;
    @Column(nullable=false, precision=14, scale=3) private BigDecimal quantity;
    @Column(nullable=false) private LocalDateTime date;
    @Column(nullable=false) private Long registeredBy;
    @Column(length=500) private String observations;
    protected InventoryMovementJpaEntity() {}
    private InventoryMovementJpaEntity(Long id, Long inventoryId, MovementType type, BigDecimal quantity,
            LocalDateTime date, Long registeredBy, String observations) {
        this.id=id; this.inventoryId=inventoryId; this.type=type; this.quantity=quantity;
        this.date=date; this.registeredBy=registeredBy; this.observations=observations;
    }
    public static InventoryMovementJpaEntity fromDomain(InventoryMovement m) {
        return new InventoryMovementJpaEntity(m.id(), m.inventoryId(), m.type(), m.quantity(),
                m.date(), m.registeredBy(), m.observations());
    }
    public InventoryMovement toDomain() {
        return new InventoryMovement(id, inventoryId, type, quantity, date, registeredBy, observations);
    }
}
