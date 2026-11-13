package com.RedFish.RedFish.inventory.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.domain.model.Product;

import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/api/v1/products", "/api/products" })
public class ProductController {

	private final CreateProductService createProductService;
	private final ListProductsService listProductsService;
	private final GetProductService getProductService;
	private final com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.SpringDataProductJpaRepository productJpa;
	private final com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.SpringDataInventoryItemJpaRepository inventoryJpa;
	private final com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataFeedingRecordJpaRepository feedingJpa;
	private final com.RedFish.RedFish.orders.infrastructure.persistence.jpa.SpringDataOrderJpaRepository orderJpa;

	public ProductController(CreateProductService createProductService, ListProductsService listProductsService,
			GetProductService getProductService,
			com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.SpringDataProductJpaRepository productJpa,
			com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.SpringDataInventoryItemJpaRepository inventoryJpa,
			com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataFeedingRecordJpaRepository feedingJpa,
			com.RedFish.RedFish.orders.infrastructure.persistence.jpa.SpringDataOrderJpaRepository orderJpa) {
		this.createProductService = createProductService;
		this.listProductsService = listProductsService;
		this.getProductService = getProductService;
		this.productJpa = productJpa; this.inventoryJpa = inventoryJpa;
		this.feedingJpa = feedingJpa; this.orderJpa = orderJpa;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
		Product product = createProductService.create(
				new Product(null, request.code(), request.name(), request.unitOfMeasure(), request.type(), true));
		return ResponseEntity.created(URI.create("/api/v1/products/" + product.id()))
			.body(ProductResponse.from(product));
	}

	@PutMapping("/{id}")
	public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
		Product product = createProductService.update(id,
				new Product(id, request.code(), request.name(), request.unitOfMeasure(), request.type(), true));
		return ProductResponse.from(product);
	}

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        createProductService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

	@DeleteMapping("/{id}/permanent")
	public ResponseEntity<Void> deletePermanently(@PathVariable Long id) {
		getProductService.get(id);
		if (inventoryJpa.existsByProductId(id) || feedingJpa.existsByProductId(id) || orderJpa.existsAnyItemForProductId(id))
			throw new IllegalStateException("No se puede eliminar el producto porque tiene inventario, alimentación o pedidos asociados. Puedes desactivarlo para conservar el historial.");
		productJpa.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public List<ProductResponse> list() {
		return listProductsService.list().stream().map(ProductResponse::from).toList();
	}

	@GetMapping("/{id}")
	public ProductResponse get(@PathVariable Long id) {
		return ProductResponse.from(getProductService.get(id));
	}
}
