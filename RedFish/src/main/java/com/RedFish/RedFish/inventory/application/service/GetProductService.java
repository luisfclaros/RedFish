package com.RedFish.RedFish.inventory.application.service;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Service
public class GetProductService {

	private final ProductRepositoryPort productRepository;

	public GetProductService(ProductRepositoryPort productRepository) {
		this.productRepository = productRepository;
	}

	public Product get(Long id) {
		return productRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("product not found"));
	}
}
