package com.RedFish.RedFish.inventory.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Service
public class ListProductsService {

	private final ProductRepositoryPort productRepository;

	public ListProductsService(ProductRepositoryPort productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> list() {
		return productRepository.findAll();
	}
}
