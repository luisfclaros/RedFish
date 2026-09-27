package com.RedFish.RedFish.contracts;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class OpenApiV1ContractTests {

	@Test
	void parsesTheVersionedOpenApiContract() throws Exception {
		try (InputStream contract = getClass().getResourceAsStream("/static/openapi-v1.yaml")) {
			assertThat(contract).isNotNull();
			JsonNode openApi = new ObjectMapper(new YAMLFactory()).readTree(contract);

			assertThat(openApi.path("openapi").asText()).isEqualTo("3.1.0");
			assertThat(openApi.path("info").path("version").asText()).isEqualTo("1.0.0");
			assertThat(openApi.path("paths").has("/api/v1/products")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/products/{id}")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/customers")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/customers/{id}")).isTrue();
			assertThat(openApi.path("components").path("schemas").has("ApiErrorResponse")).isTrue();
		}
	}
}
