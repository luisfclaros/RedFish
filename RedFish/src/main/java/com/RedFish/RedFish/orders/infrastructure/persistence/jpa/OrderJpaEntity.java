package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderStatus;
import jakarta.persistence.*;

@Entity
@Table(name="pedidos")
public class OrderJpaEntity {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private Long customerId;
    @Column(nullable=false) private LocalDate date;
    @Enumerated(EnumType.STRING) @Column(nullable=false, length=30) private OrderStatus status;
    @Column(length=500) private String observations;
    @ElementCollection
    @CollectionTable(name="pedido_detalles", joinColumns=@JoinColumn(name="order_id"))
    private List<OrderItemEmbeddable> items = new ArrayList<>();
    protected OrderJpaEntity() {}
    private OrderJpaEntity(Long id, Long customerId, LocalDate date, OrderStatus status, String observations,
            List<OrderItemEmbeddable> items) {
        this.id=id; this.customerId=customerId; this.date=date; this.status=status;
        this.observations=observations; this.items=items;
    }
    public static OrderJpaEntity fromDomain(Order o) {
        return new OrderJpaEntity(o.id(), o.customerId(), o.date(), o.status(), o.observations(),
                o.items().stream().map(OrderItemEmbeddable::fromDomain).toList());
    }
    public Order toDomain() {
        return Order.restore(id, customerId, date, items.stream().map(OrderItemEmbeddable::toDomain).toList(),
                observations, status);
    }
}
