package com.RedFish.RedFish.shared.domain;

import java.math.BigDecimal;

public final class DomainValidation {

	private DomainValidation() {
	}

	public static String requireText(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " is required");
		}
		return value.trim();
	}

	public static <T> T requireNonNull(T value, String fieldName) {
		if (value == null) {
			throw new IllegalArgumentException(fieldName + " is required");
		}
		return value;
	}

	public static BigDecimal requirePositive(BigDecimal value, String fieldName) {
		requireNonNull(value, fieldName);
		if (value.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException(fieldName + " must be greater than zero");
		}
		return value;
	}

	public static BigDecimal requireNonNegative(BigDecimal value, String fieldName) {
		requireNonNull(value, fieldName);
		if (value.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException(fieldName + " cannot be negative");
		}
		return value;
	}
}
