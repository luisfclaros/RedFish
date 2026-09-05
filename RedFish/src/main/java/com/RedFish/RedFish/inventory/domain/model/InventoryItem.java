package com.RedFish.RedFish.inventory.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNegative;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requirePositive;

import java.math.BigDecimal;

public class InventoryItem {

	private final Long id;
	private final Long productId;
	private BigDecimal quantity;
	private final BigDecimal minimumStock;

	public InventoryItem(Long id, Long productId, BigDecimal quantity, BigDecimal minimumStock) {
		this.id = id;
		this.productId = requireNonNull(productId, "product id");
		this.quantity = requireNonNegative(quantity, "inventory quantity");
		this.minimumStock = requireNonNegative(minimumStock, "minimum stock");
	}

	public void increase(BigDecimal amount) {
		this.quantity = this.quantity.add(requirePositive(amount, "increase amount"));
	}

	public void decrease(BigDecimal amount) {
		BigDecimal requestedAmount = requirePositive(amount, "decrease amount");
		if (this.quantity.compareTo(requestedAmount) < 0) {
			throw new IllegalStateException("inventory stock cannot be negative");
		}
		this.quantity = this.quantity.subtract(requestedAmount);
	}

	public boolean belowMinimumStock() {
		return quantity.compareTo(minimumStock) < 0;
	}

	public Long id() {
		return id;
	}

	public Long productId() {
		return productId;
	}

	public BigDecimal quantity() {
		return quantity;
	}

	public BigDecimal minimumStock() {
		return minimumStock;
	}
}
