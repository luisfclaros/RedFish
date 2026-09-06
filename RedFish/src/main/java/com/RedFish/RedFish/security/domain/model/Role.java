package com.RedFish.RedFish.security.domain.model;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

public record Role(Long id, String name, String description) {

	public Role {
		name = requireText(name, "role name");
	}
}
