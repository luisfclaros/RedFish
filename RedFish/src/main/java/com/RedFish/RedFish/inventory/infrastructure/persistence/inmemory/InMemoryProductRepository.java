package com.RedFish.RedFish.inventory.infrastructure.persistence.inmemory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Repository
public class InMemoryProductRepository implements ProductRepositoryPort {

	private final ConcurrentMap<Long, Product> products = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0);

	@Override
	public Product save(Product product) {
		Long id = product.id() == null ? sequence.incrementAndGet() : product.id();
		Product storedProduct = new Product(id, product.code(), product.name(), product.unitOfMeasure(), product.type(),
				product.active());
		products.put(id, storedProduct);
		return storedProduct;
	}

	@Override
	public List<Product> findAll() {
		return products.values()
			.stream()
			.sorted(Comparator.comparing(Product::id))
			.toList();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return Optional.ofNullable(products.get(id));
	}

	@Override
	public boolean existsByCode(String code) {
		return products.values().stream().anyMatch(product -> product.code().equalsIgnoreCase(code));
	}
}
