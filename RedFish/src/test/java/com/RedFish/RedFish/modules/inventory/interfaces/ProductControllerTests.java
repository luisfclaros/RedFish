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
import com.RedFish.RedFish.shared.interfaces.rest.LegacyApiDeprecationFilter;

class ProductControllerTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		InMemoryProductRepository repository = new InMemoryProductRepository();
		ProductController controller = new ProductController(new CreateProductService(repository, event -> {
		}),
				new ListProductsService(repository), new GetProductService(repository));
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setControllerAdvice(new RestExceptionHandler())
			.addFilters(new LegacyApiDeprecationFilter())
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

		mockMvc.perform(post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/v1/products/1"))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.code").value("PROD-001"))
			.andExpect(jsonPath("$.name").value("Tilapia Roja"));

		mockMvc.perform(get("/api/v1/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].code").value("PROD-001"));

		mockMvc.perform(get("/api/v1/products/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("PROD-001"));
	}

	@Test
	void rejectsInvalidProductRequest() throws Exception {
		String traceId = "9e289660-5f90-4c65-9f8f-939c91128295";
		String requestBody = """
				{
				  "code": "",
				  "name": "Tilapia Roja",
				  "unitOfMeasure": "kg",
				  "type": "PRODUCT"
				}
				""";

		mockMvc.perform(post("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Trace-Id", traceId)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(header().string("X-Trace-Id", traceId))
			.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.error.message").value("invalid request body"))
			.andExpect(jsonPath("$.error.details.code").exists())
			.andExpect(jsonPath("$.error.trace_id").value(traceId));
	}

	@Test
	void returnsStandardErrorWhenProductDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/v1/products/999"))
			.andExpect(status().isBadRequest())
			.andExpect(header().exists("X-Trace-Id"))
			.andExpect(jsonPath("$.error.code").value("RESOURCE_NOT_FOUND"))
			.andExpect(jsonPath("$.error.message").value("product not found"))
			.andExpect(jsonPath("$.error.details").isMap())
			.andExpect(jsonPath("$.error.trace_id").isNotEmpty());
	}

	@Test
	void returnsStandardErrorWhenIdentifierCannotBeParsed() throws Exception {
		mockMvc.perform(get("/api/v1/products/not-a-number"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error.code").value("INVALID_REQUEST"))
			.andExpect(jsonPath("$.error.message").value("request could not be parsed"));
	}

	@Test
	void keepsLegacyProductRouteAvailable() throws Exception {
		String requestBody = """
				{
				  "code": "PROD-LEGACY",
				  "name": "Producto legado",
				  "unitOfMeasure": "kg",
				  "type": "PRODUCT"
				}
				""";

		mockMvc.perform(post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/v1/products/1"))
			.andExpect(header().string("Deprecation", "true"));

		mockMvc.perform(get("/api/products/1"))
			.andExpect(status().isOk())
			.andExpect(header().string("Deprecation", "true"))
			.andExpect(header().exists("Sunset"))
			.andExpect(header().string("Link", "</api/v1/products/1>; rel=\"successor-version\""));
	}
}
