package com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa;

import java.time.LocalDate;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "despachos")
public class DispatchJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false) private Long orderId;
    @Column(nullable=false) private Long vehicleId;
    @Column(nullable=false) private LocalDate dispatchDate;
    private LocalDate deliveryDate;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=20)
    private DispatchStatus status;
    @Column(nullable=false, length=250) private String deliveryAddress;
    @Column(length=100) private String receivedBy;

    protected DispatchJpaEntity() {}

    private DispatchJpaEntity(Long id, Long orderId, Long vehicleId, LocalDate dispatchDate,
            LocalDate deliveryDate, DispatchStatus status, String deliveryAddress, String receivedBy) {
        this.id=id; this.orderId=orderId; this.vehicleId=vehicleId; this.dispatchDate=dispatchDate;
        this.deliveryDate=deliveryDate; this.status=status; this.deliveryAddress=deliveryAddress; this.receivedBy=receivedBy;
    }

    public static DispatchJpaEntity fromDomain(Dispatch d) {
        return new DispatchJpaEntity(d.id(), d.orderId(), d.vehicleId(), d.dispatchDate(), d.deliveryDate(),
                d.status(), d.deliveryAddress(), d.receivedBy());
    }

    public Dispatch toDomain() {
        return Dispatch.restore(id, orderId, vehicleId, dispatchDate, deliveryDate, status, deliveryAddress, receivedBy);
    }
}
