package com.RedFish.RedFish.security.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.LocalDateTime;

public class User {

	private final Long id;
	private final String name;
	private final String username;
	private final String passwordHash;
	private final Role role;
	private final LocalDateTime createdAt;
	private boolean active;

	public User(Long id, String name, String username, String passwordHash, Role role, boolean active,
			LocalDateTime createdAt) {
		this.id = id;
		this.name = requireText(name, "user name");
		this.username = requireText(username, "username");
		this.passwordHash = requireText(passwordHash, "password hash");
		this.role = requireNonNull(role, "role");
		this.active = active;
		this.createdAt = requireNonNull(createdAt, "created at");
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	public Long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String username() {
		return username;
	}

	public String passwordHash() {
		return passwordHash;
	}

	public Role role() {
		return role;
	}

	public boolean active() {
		return active;
	}

	public LocalDateTime createdAt() {
		return createdAt;
	}
}
