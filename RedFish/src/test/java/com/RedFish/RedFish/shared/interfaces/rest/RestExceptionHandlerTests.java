package com.RedFish.RedFish.shared.interfaces.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.ResponseEntity;

class RestExceptionHandlerTests {

	private final RestExceptionHandler handler = new RestExceptionHandler();

	@Test
	void mapsMissingResourcesToNotFound() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		ResponseEntity<ApiErrorResponse> response = handler.handleIllegalArgument(
				new IllegalArgumentException("product not found"), request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.getBody().error().code()).isEqualTo("RESOURCE_NOT_FOUND");
		assertThat(response.getHeaders().getFirst("X-Trace-Id")).isNotBlank();
	}

	@Test
	void mapsInvalidBusinessArgumentsToBadRequest() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		ResponseEntity<ApiErrorResponse> response = handler.handleIllegalArgument(
				new IllegalArgumentException("product code already exists"), request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.getBody().error().code()).isEqualTo("BUSINESS_RULE_VIOLATION");
	}

	@Test
	void mapsInvalidBusinessStateToConflict() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		ResponseEntity<ApiErrorResponse> response = handler.handleIllegalState(
				new IllegalStateException("invalid order status transition"), request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(response.getBody().error().code()).isEqualTo("BUSINESS_STATE_CONFLICT");
	}
}
