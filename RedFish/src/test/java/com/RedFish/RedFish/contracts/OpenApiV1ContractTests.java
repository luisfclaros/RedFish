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
			assertThat(openApi.path("paths").has("/api/v1/vehicles")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/ponds")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/feeding-records")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/inventory")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/orders")).isTrue();
			assertThat(openApi.path("paths").has("/api/v1/dispatches")).isTrue();
			assertThat(openApi.path("components").path("securitySchemes").has("basicAuth")).isTrue();
			assertThat(openApi.path("components").path("schemas").has("ApiErrorResponse")).isTrue();

            // Keep the published API description aligned with implemented lifecycle operations.
            assertThat(openApi.path("paths").path("/api/v1/products/{id}").has("put")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/products/{id}").has("delete")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/customers/{id}").has("put")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/customers/{id}").has("delete")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/vehicles/{id}").has("put")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/ponds/{id}").has("put")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/orders/{id}/in-preparation").has("post")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/orders/{id}/cancel").has("post")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/dispatches/{id}/in-transit").has("post")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/dispatches/{id}/delivered").has("post")).isTrue();
            assertThat(openApi.path("paths").path("/api/v1/inventory/stock").path("post")
                    .path("requestBody").path("content").path("application/json").path("schema")
                    .path("$ref").asText()).isEqualTo("#/components/schemas/InventoryStockRequest");
            assertThat(openApi.path("paths").path("/api/v1/orders").path("post")
                    .path("requestBody").path("content").path("application/json").path("schema")
                    .path("$ref").asText()).isEqualTo("#/components/schemas/OrderRequest");
            assertThat(openApi.path("paths").path("/api/v1/dispatches").path("post")
                    .path("requestBody").path("content").path("application/json").path("schema")
                    .path("$ref").asText()).isEqualTo("#/components/schemas/ScheduleDispatchRequest");
		}
	}
}
