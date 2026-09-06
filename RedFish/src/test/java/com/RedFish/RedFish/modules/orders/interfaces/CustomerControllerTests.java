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

class CustomerControllerTests {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
		CustomerController controller = new CustomerController(new CreateCustomerService(repository),
				new ListCustomersService(repository), new GetCustomerService(repository));
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setControllerAdvice(new RestExceptionHandler())
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

		mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", "/api/customers/1"))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("Restaurante El Lago"))
			.andExpect(jsonPath("$.email").value("compras@ellago.com"));

		mockMvc.perform(get("/api/customers"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].name").value("Restaurante El Lago"));

		mockMvc.perform(get("/api/customers/1"))
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

		mockMvc.perform(post("/api/customers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.error").value("invalid request body"));
	}
}
