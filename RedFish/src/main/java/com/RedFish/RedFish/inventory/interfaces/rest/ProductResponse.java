package com.RedFish.RedFish.inventory.interfaces.rest;

import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;

public record ProductResponse(Long id, String code, String name, String unitOfMeasure, ProductType type,
		boolean active) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.id(), product.code(), product.name(), product.unitOfMeasure(),
				product.type(), product.active());
	}
}
