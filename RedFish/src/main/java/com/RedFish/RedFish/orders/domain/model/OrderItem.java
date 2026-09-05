package com.RedFish.RedFish.orders.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requirePositive;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.math.BigDecimal;

public record OrderItem(Long productId, String productName, BigDecimal quantity, BigDecimal unitPrice) {

	public OrderItem {
		requireNonNull(productId, "product id");
		productName = requireText(productName, "product name");
		quantity = requirePositive(quantity, "order item quantity");
		unitPrice = requirePositive(unitPrice, "order item unit price");
	}

	public BigDecimal subtotal() {
		return quantity.multiply(unitPrice);
	}
}
