package com.RedFish.RedFish.inventory.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Service
public class CreateProductService {

	private final ProductRepositoryPort productRepository;

	public CreateProductService(ProductRepositoryPort productRepository) {
		this.productRepository = requireNonNull(productRepository, "product repository");
	}

	public Product create(Product product) {
		Product requestedProduct = requireNonNull(product, "product");
		if (productRepository.existsByCode(requestedProduct.code())) {
			throw new IllegalArgumentException("product code already exists");
		}
		return productRepository.save(requestedProduct);
	}
}
