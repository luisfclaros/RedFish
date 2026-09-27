package com.RedFish.RedFish.shared.interfaces.rest;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiError(String code, String message, Map<String, String> details,
		@JsonProperty("trace_id") String traceId) {
}
