package com.RedFish.RedFish.contracts.provider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.inventory.infrastructure.persistence.inmemory.InMemoryProductRepository;
import com.RedFish.RedFish.inventory.interfaces.rest.ProductController;
import com.RedFish.RedFish.shared.interfaces.rest.RestExceptionHandler;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.spring.spring7.PactVerificationSpring7Provider;
import au.com.dius.pact.provider.spring.spring7.Spring7MockMvcTestTarget;

@Provider("redfish-api")
@PactFolder("pacts")
class ProductApiProviderPactTests {

	private CreateProductService createProductService;

	@BeforeEach
	void setUp(PactVerificationContext context) {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		createProductService = new CreateProductService(repository, event -> {
		});
		ProductController controller = new ProductController(createProductService,
				new ListProductsService(repository), new GetProductService(repository));
		Spring7MockMvcTestTarget target = new Spring7MockMvcTestTarget();
		target.setControllers(controller);
		target.setControllerAdvices(new RestExceptionHandler());
		context.setTarget(target);
	}

	@State("product 1 exists")
	void productExists() {
		createProductService.create(
				new Product(null, "PROD-001", "Tilapia Roja", "kg", ProductType.PRODUCT, true));
	}

	@TestTemplate
	@ExtendWith(PactVerificationSpring7Provider.class)
	void verifiesConsumerContract(PactVerificationContext context) {
		context.verifyInteraction();
	}
}
