package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;

@Repository
public class JpaProductRepository implements ProductRepositoryPort {

	private final SpringDataProductJpaRepository repository;

	public JpaProductRepository(SpringDataProductJpaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Product save(Product product) {
		return repository.save(ProductJpaEntity.fromDomain(product)).toDomain();
	}

	@Override
	public List<Product> findAll() {
		return repository.findAll().stream().map(ProductJpaEntity::toDomain).toList();
	}

	@Override
	public Optional<Product> findById(Long id) {
		return repository.findById(id).map(ProductJpaEntity::toDomain);
	}

	@Override
	public boolean existsByCode(String code) {
		return repository.existsByCodeIgnoreCase(code);
	}
}
