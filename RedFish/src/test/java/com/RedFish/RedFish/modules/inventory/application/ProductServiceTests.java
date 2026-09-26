package com.RedFish.RedFish.modules.inventory.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.inventory.application.event.ProductCreatedEvent;
import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.inventory.infrastructure.persistence.inmemory.InMemoryProductRepository;

class ProductServiceTests {

	@Test
	void createsAndListsProductsUsingRepositoryPort() {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		AtomicReference<ProductCreatedEvent> publishedEvent = new AtomicReference<>();
		CreateProductService createProductService = new CreateProductService(repository, publishedEvent::set);
		ListProductsService listProductsService = new ListProductsService(repository);

		Product product = createProductService.create(new Product(null, "PROD-001", "Tilapia Roja", "kg",
				ProductType.PRODUCT, true));

		assertThat(product.id()).isNotNull();
		assertThat(listProductsService.list()).hasSize(1);
		assertThat(publishedEvent.get().productId()).isEqualTo(product.id());
		assertThat(publishedEvent.get().productCode()).isEqualTo("PROD-001");
	}

	@Test
	void rejectsDuplicatedProductCode() {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		CreateProductService createProductService = new CreateProductService(repository, event -> {
		});
		createProductService.create(new Product(null, "PROD-001", "Tilapia Roja", "kg", ProductType.PRODUCT, true));

		assertThatThrownBy(() -> createProductService.create(
				new Product(null, "PROD-001", "Tilapia Premium", "kg", ProductType.PRODUCT, true)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already exists");
	}

	@Test
	void getsProductById() {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		CreateProductService createProductService = new CreateProductService(repository, event -> {
		});
		GetProductService getProductService = new GetProductService(repository);
		Product product = createProductService.create(new Product(null, "INS-001", "Alimento", "kg",
				ProductType.INPUT, true));

		assertThat(getProductService.get(product.id()).code()).isEqualTo("INS-001");
	}
}
