package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import java.math.BigDecimal;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import jakarta.persistence.*;

@Embeddable
public class OrderItemEmbeddable {
    @Column(nullable=false) private Long productId;
    @Column(nullable=false, length=100) private String productName;
    @Column(nullable=false, precision=14, scale=3) private BigDecimal quantity;
    @Column(nullable=false, precision=14, scale=2) private BigDecimal unitPrice;
    protected OrderItemEmbeddable() {}
    private OrderItemEmbeddable(Long productId, String productName, BigDecimal quantity, BigDecimal unitPrice) {
        this.productId=productId; this.productName=productName; this.quantity=quantity; this.unitPrice=unitPrice;
    }
    public static OrderItemEmbeddable fromDomain(OrderItem i) {
        return new OrderItemEmbeddable(i.productId(), i.productName(), i.quantity(), i.unitPrice());
    }
    public OrderItem toDomain() { return new OrderItem(productId, productName, quantity, unitPrice); }
}
