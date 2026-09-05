package com.RedFish.RedFish.inventory.infrastructure.persistence.jpa;

import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class ProductJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 20)
	private String code;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 20)
	private String unitOfMeasure;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ProductType type;

	@Column(nullable = false)
	private boolean active;

	protected ProductJpaEntity() {
	}

	private ProductJpaEntity(Long id, String code, String name, String unitOfMeasure, ProductType type, boolean active) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.unitOfMeasure = unitOfMeasure;
		this.type = type;
		this.active = active;
	}

	public static ProductJpaEntity fromDomain(Product product) {
		return new ProductJpaEntity(product.id(), product.code(), product.name(), product.unitOfMeasure(),
				product.type(), product.active());
	}

	public Product toDomain() {
		return new Product(id, code, name, unitOfMeasure, type, active);
	}

	public Long getId() {
		return id;
	}

	public String getCode() {
		return code;
	}
}
