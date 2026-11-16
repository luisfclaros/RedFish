package com.RedFish.RedFish.contracts;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.RedFish.RedFish.shared.interfaces.rest.ApiContractController;

class ApiContractControllerTests {

	@Test
	void publishesTheOpenApiContractAsYaml() throws Exception {
		MockMvcBuilders.standaloneSetup(new ApiContractController())
			.build()
			.perform(get("/openapi-v1.yaml"))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/yaml"))
			.andExpect(content().string(containsString("openapi: 3.1.0")));
	}
}
