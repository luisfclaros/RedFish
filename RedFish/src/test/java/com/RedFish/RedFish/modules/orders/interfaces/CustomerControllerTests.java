package com.RedFish.RedFish.modules.orders.interfaces;

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

import com.RedFish.RedFish.orders.application.service.CreateCustomerService;
import com.RedFish.RedFish.orders.application.service.GetCustomerService;
import com.RedFish.RedFish.orders.application.service.ListCustomersService;
import com.RedFish.RedFish.orders.infrastructure.persistence.inmemory.InMemoryCustomerRepository;
import com.RedFish.RedFish.orders.interfaces.rest.CustomerController;
import com.RedFish.RedFish.shared.interfaces.rest.RestExceptionHandler;
import com.RedFish.RedFish.shared.interfaces.rest.LegacyApiDeprecationFilter;

class CustomerControllerTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
		CustomerController controller = new CustomerController(new CreateCustomerService(repository),
				new ListCustomersService(repository), new GetCustomerService(repository));
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setControllerAdvice(new RestExceptionHandler())
			.addFilters(new LegacyApiDeprecationFilter())
			.build();
	}

	@Test
	void createsAndRetrievesCustomerThroughHttp() throws Exception {
		String requestBody = """
				{
				  "name": "Restaurante El Lago",
				  "phone": "3001234567",
				  "address": "Calle 10 # 15-20",
				  "email": "compras@ellago.com"
				}
				""";

		mockMvc.perform(post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/v1/customers/1"))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Restaurante El Lago"))
			.andExpect(jsonPath("$.email").value("compras@ellago.com"));

		mockMvc.perform(get("/api/v1/customers"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name").value("Restaurante El Lago"));

		mockMvc.perform(get("/api/v1/customers/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.phone").value("3001234567"));
	}

	@Test
	void rejectsInvalidCustomerRequest() throws Exception {
		String requestBody = """
				{
				  "name": "",
				  "phone": "3001234567",
				  "address": "Calle 10 # 15-20",
				  "email": "not-an-email"
				}
				""";

		mockMvc.perform(post("/api/v1/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(header().exists("X-Trace-Id"))
			.andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
			.andExpect(jsonPath("$.error.message").value("invalid request body"))
			.andExpect(jsonPath("$.error.details.name").exists())
			.andExpect(jsonPath("$.error.details.email").exists())
			.andExpect(jsonPath("$.error.trace_id").isNotEmpty());
	}

	@Test
	void keepsLegacyCustomerRouteAvailable() throws Exception {
		mockMvc.perform(get("/api/customers"))
			.andExpect(status().isOk())
			.andExpect(header().string("Deprecation", "true"))
			.andExpect(header().exists("Sunset"))
			.andExpect(header().string("Link", "</api/v1/customers>; rel=\"successor-version\""));
	}
}
