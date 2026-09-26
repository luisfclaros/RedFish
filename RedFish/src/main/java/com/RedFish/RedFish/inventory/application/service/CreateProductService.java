package com.RedFish.RedFish.inventory.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.inventory.application.port.ProductCreatedEventPublisher;
import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Service
public class CreateProductService {

	private final ProductRepositoryPort productRepository;
	private final ProductCreatedEventPublisher eventPublisher;

	public CreateProductService(ProductRepositoryPort productRepository, ProductCreatedEventPublisher eventPublisher) {
		this.productRepository = requireNonNull(productRepository, "product repository");
		this.eventPublisher = requireNonNull(eventPublisher, "product event publisher");
	}

	@Transactional
	public Product create(Product product) {
		Product requestedProduct = requireNonNull(product, "product");
		if (productRepository.existsByCode(requestedProduct.code())) {
			throw new IllegalArgumentException("product code already exists");
		}
		Product createdProduct = productRepository.save(requestedProduct);
		eventPublisher.publish(ProductCreatedEvent.from(createdProduct));
		return createdProduct;
	}
}
