package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.RedFish.RedFish.production.domain.model.FeedingRecord;

import jakarta.persistence.*;

@Entity
@Table(name = "registros_alimentacion")
public class FeedingRecordJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long pondId;
    private Long productId;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false, length = 100)
    private String foodType;
    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;
    @Column(nullable = false)
    private Long registeredBy;
    @Column(length = 500)
    private String observations;

    protected FeedingRecordJpaEntity() {}

    private FeedingRecordJpaEntity(Long id, Long pondId, Long productId, LocalDate date, String foodType,
            BigDecimal quantity, Long registeredBy, String observations) {
        this.id=id; this.pondId=pondId; this.productId=productId; this.date=date; this.foodType=foodType;
        this.quantity=quantity; this.registeredBy=registeredBy; this.observations=observations;
    }

    public static FeedingRecordJpaEntity fromDomain(FeedingRecord record) {
        return new FeedingRecordJpaEntity(record.id(), record.pondId(), record.productId(), record.date(), record.foodType(),
                record.quantity(), record.registeredBy(), record.observations());
    }

    public FeedingRecord toDomain() {
        return new FeedingRecord(id, pondId, productId, date, foodType, quantity, registeredBy, observations);
    }
}
