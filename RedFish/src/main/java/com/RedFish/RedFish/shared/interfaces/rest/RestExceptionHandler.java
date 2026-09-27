package com.RedFish.RedFish.shared.interfaces.rest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);
	private static final String TRACE_HEADER = "X-Trace-Id";

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException exception,
			HttpServletRequest request) {
		String code = exception.getMessage() != null && exception.getMessage().contains("not found")
				? "RESOURCE_NOT_FOUND"
				: "BUSINESS_RULE_VIOLATION";
		return response(HttpStatus.BAD_REQUEST, code, exception.getMessage(), Map.of(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		Map<String, String> details = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(error -> details.putIfAbsent(error.getField(),
				error.getDefaultMessage() == null ? "invalid value" : error.getDefaultMessage()));
		return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "invalid request body", details, request);
	}

	@ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ApiErrorResponse> handleMalformedRequest(Exception exception, HttpServletRequest request) {
		return response(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "request could not be parsed", Map.of(), request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
		String traceId = traceId(request);
		LOGGER.error("Unexpected API error with trace id {}", traceId, exception);
		return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "unexpected server error", Map.of(),
				traceId);
	}

	private ResponseEntity<ApiErrorResponse> response(HttpStatus status, String code, String message,
			Map<String, String> details, HttpServletRequest request) {
		return response(status, code, message, details, traceId(request));
	}

	private ResponseEntity<ApiErrorResponse> response(HttpStatus status, String code, String message,
			Map<String, String> details, String traceId) {
		ApiErrorResponse body = new ApiErrorResponse(new ApiError(code, message, details, traceId));
		return ResponseEntity.status(status).header(TRACE_HEADER, traceId).body(body);
	}

	private String traceId(HttpServletRequest request) {
		String providedTraceId = request.getHeader(TRACE_HEADER);
		if (providedTraceId != null && !providedTraceId.isBlank()) {
			try {
				return UUID.fromString(providedTraceId).toString();
			}
			catch (IllegalArgumentException invalidTraceId) {
				// Invalid external correlation values are replaced with a valid local UUID.
			}
		}
		return UUID.randomUUID().toString();
	}
}
