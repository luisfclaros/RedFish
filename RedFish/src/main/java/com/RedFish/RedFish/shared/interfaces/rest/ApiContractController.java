package com.RedFish.RedFish.shared.interfaces.rest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiContractController {

	@GetMapping(value = "/openapi-v1.yaml", produces = "application/yaml")
	public Resource openApiV1() {
		return new ClassPathResource("static/openapi-v1.yaml");
	}
}
