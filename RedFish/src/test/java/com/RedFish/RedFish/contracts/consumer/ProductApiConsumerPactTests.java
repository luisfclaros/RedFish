package com.RedFish.RedFish.contracts.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;

@ExtendWith(PactConsumerTestExt.class)
class ProductApiConsumerPactTests {

	private static final String PROVIDER = "redfish-api";
	private static final String CONSUMER = "redfish-web-client";

	@Pact(provider = PROVIDER, consumer = CONSUMER)
	public V4Pact productByIdPact(PactBuilder builder) {
		PactDslJsonBody body = new PactDslJsonBody()
			.integerType("id", 1)
			.stringType("code", "PROD-001")
			.stringType("name", "Tilapia Roja")
			.stringType("unitOfMeasure", "kg")
			.stringType("type", "PRODUCT")
			.booleanType("active", true);

		return builder.usingLegacyDsl()
			.given("product 1 exists")
			.uponReceiving("a request for an existing product")
			.path("/api/v1/products/1")
			.method("GET")
			.headers("Accept", "application/json")
			.willRespondWith()
			.status(200)
			.headers(Map.of("Content-Type", "application/json"))
			.body(body)
			.toPact(V4Pact.class);
	}

	@Test
	@PactTestFor(providerName = PROVIDER, pactMethod = "productByIdPact")
	void consumerReadsTheVersionedProductContract(MockServer mockServer) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(mockServer.getUrl() + "/api/v1/products/1"))
			.header("Accept", "application/json")
			.GET()
			.build();
		HttpResponse<String> response = HttpClient.newHttpClient()
			.send(request, HttpResponse.BodyHandlers.ofString());
		JsonNode product = new ObjectMapper().readTree(response.body());

		assertThat(response.statusCode()).isEqualTo(200);
		assertThat(product.get("id").asLong()).isEqualTo(1L);
		assertThat(product.get("code").asText()).isEqualTo("PROD-001");
		assertThat(product.get("active").asBoolean()).isTrue();
	}
}
