package com.RedFish.RedFish.production.infrastructure.persistence.jpa;

import java.math.BigDecimal;

import com.RedFish.RedFish.production.domain.model.Pond;

import jakarta.persistence.*;

@Entity
@Table(name = "estanques")
public class PondJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String code;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 150)
    private String location;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal capacity;
    @Column(nullable = false)
    private boolean active;

    protected PondJpaEntity() {}

    private PondJpaEntity(Long id, String code, String name, String location, BigDecimal capacity, boolean active) {
        this.id = id; this.code = code; this.name = name; this.location = location;
        this.capacity = capacity; this.active = active;
    }

    public static PondJpaEntity fromDomain(Pond pond) {
        return new PondJpaEntity(pond.id(), pond.code(), pond.name(), pond.location(), pond.capacity(), pond.active());
    }

    public Pond toDomain() {
        return new Pond(id, code, name, location, capacity, active);
    }
}
