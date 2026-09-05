package com.RedFish.RedFish.inventory.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.domain.model.Product;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	private final CreateProductService createProductService;
	private final ListProductsService listProductsService;
	private final GetProductService getProductService;

	public ProductController(CreateProductService createProductService, ListProductsService listProductsService,
			GetProductService getProductService) {
		this.createProductService = createProductService;
		this.listProductsService = listProductsService;
		this.getProductService = getProductService;
	}

	@PostMapping
	public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
		Product product = createProductService.create(
				new Product(null, request.code(), request.name(), request.unitOfMeasure(), request.type(), true));
		return ResponseEntity.created(URI.create("/api/products/" + product.id()))
			.body(ProductResponse.from(product));
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
