package com.RedFish.RedFish.modules.inventory.interfaces;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.RedFish.RedFish.inventory.application.service.CreateProductService;
import com.RedFish.RedFish.inventory.application.service.GetProductService;
import com.RedFish.RedFish.inventory.application.service.ListProductsService;
import com.RedFish.RedFish.inventory.infrastructure.persistence.inmemory.InMemoryProductRepository;
import com.RedFish.RedFish.inventory.interfaces.rest.ProductController;
import com.RedFish.RedFish.shared.interfaces.rest.RestExceptionHandler;

class ProductControllerTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		ProductController controller = new ProductController(new CreateProductService(repository),
				new ListProductsService(repository), new GetProductService(repository));
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setControllerAdvice(new RestExceptionHandler())
			.build();
	}

	@Test
	void createsAndRetrievesProductThroughHttp() throws Exception {
		String requestBody = """
				{
				  "code": "PROD-001",
				  "name": "Tilapia Roja",
				  "unitOfMeasure": "kg",
				  "type": "PRODUCT"
				}
				""";

		mockMvc.perform(post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/products/1"))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.code").value("PROD-001"))
			.andExpect(jsonPath("$.name").value("Tilapia Roja"));

		mockMvc.perform(get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].code").value("PROD-001"));

		mockMvc.perform(get("/api/products/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("PROD-001"));
	}

	@Test
	void rejectsInvalidProductRequest() throws Exception {
		String requestBody = """
				{
				  "code": "",
				  "name": "Tilapia Roja",
				  "unitOfMeasure": "kg",
				  "type": "PRODUCT"
				}
				""";

		mockMvc.perform(post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("invalid request body"));
	}
}
