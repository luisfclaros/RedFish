package com.RedFish.RedFish.inventory.application.port;

import java.util.List;
import java.util.Optional;

import com.RedFish.RedFish.inventory.domain.model.Product;

public interface ProductRepositoryPort {

	Product save(Product product);

	List<Product> findAll();

	Optional<Product> findById(Long id);

	boolean existsByCode(String code);
}
