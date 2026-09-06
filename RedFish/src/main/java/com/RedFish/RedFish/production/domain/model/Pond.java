package com.RedFish.RedFish.production.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNegative;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.math.BigDecimal;

public class Pond {

	private final Long id;
	private final String code;
	private final String name;
	private final String location;
	private final BigDecimal capacity;
	private boolean active;

	public Pond(Long id, String code, String name, String location, BigDecimal capacity, boolean active) {
		this.id = id;
		this.code = requireText(code, "pond code");
		this.name = requireText(name, "pond name");
		this.location = requireText(location, "pond location");
		this.capacity = requireNonNegative(capacity, "pond capacity");
		this.active = active;
	}

	public void ensureCanReceiveFeedingRecord() {
		if (!active) {
			throw new IllegalStateException("inactive ponds cannot receive feeding records");
		}
	}

	public Long id() {
		return id;
	}

	public String code() {
		return code;
	}

	public String name() {
		return name;
	}

	public String location() {
		return location;
	}

	public BigDecimal capacity() {
		return capacity;
	}

	public boolean active() {
		return active;
	}
}
