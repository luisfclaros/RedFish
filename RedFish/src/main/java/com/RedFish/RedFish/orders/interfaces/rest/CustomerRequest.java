package com.RedFish.RedFish.orders.interfaces.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
		@NotBlank String name,
		@NotBlank String phone,
		@NotBlank String address,
		@Email String email) {
}
