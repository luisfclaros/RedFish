package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import jakarta.persistence.*;

@Entity
@Table(name="inventario")
public class InventoryItemJpaEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private Long productId;
    @Column(nullable=false, precision=14, scale=3) private BigDecimal quantity;
    @Column(nullable=false, precision=14, scale=3) private BigDecimal minimumStock;
    protected InventoryItemJpaEntity() {}
    private InventoryItemJpaEntity(Long id, Long productId, BigDecimal quantity, BigDecimal minimumStock) {
        this.id=id; this.productId=productId; this.quantity=quantity; this.minimumStock=minimumStock;
    }
    public static InventoryItemJpaEntity fromDomain(InventoryItem i) {
        return new InventoryItemJpaEntity(i.id(), i.productId(), i.quantity(), i.minimumStock());
    }
    public InventoryItem toDomain() { return new InventoryItem(id, productId, quantity, minimumStock); }
}
