package com.RedFish.RedFish.inventory.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

public class Product {

	private final Long id;
	private final String code;
	private final String name;
	private final String unitOfMeasure;
	private final ProductType type;
	private boolean active;

	public Product(Long id, String code, String name, String unitOfMeasure, ProductType type, boolean active) {
		this.id = id;
		this.code = requireText(code, "product code");
		this.name = requireText(name, "product name");
		this.unitOfMeasure = requireText(unitOfMeasure, "unit of measure");
		this.type = requireNonNull(type, "product type");
		this.active = active;
	}

	public void ensureCanBeUsed() {
		if (!active) {
			throw new IllegalStateException("inactive products cannot be used");
		}
	}

	public Long id() {
		return id;
	}

	public String code() {
		return code;
	}

	public String name() {
		return name;
	}

	public String unitOfMeasure() {
		return unitOfMeasure;
	}

	public ProductType type() {
		return type;
	}

	public boolean active() {
		return active;
	}
}
