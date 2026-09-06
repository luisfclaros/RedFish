package com.RedFish.RedFish.orders.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RedFish.RedFish.orders.application.service.CreateCustomerService;
import com.RedFish.RedFish.orders.application.service.GetCustomerService;
import com.RedFish.RedFish.orders.application.service.ListCustomersService;
import com.RedFish.RedFish.orders.domain.model.Customer;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CreateCustomerService createCustomerService;
	private final ListCustomersService listCustomersService;
	private final GetCustomerService getCustomerService;

	public CustomerController(CreateCustomerService createCustomerService, ListCustomersService listCustomersService,
			GetCustomerService getCustomerService) {
		this.createCustomerService = createCustomerService;
		this.listCustomersService = listCustomersService;
		this.getCustomerService = getCustomerService;
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
		Customer customer = createCustomerService.create(new Customer(null, request.name(), request.phone(),
				request.address(), request.email(), true));
		return ResponseEntity.created(URI.create("/api/customers/" + customer.id()))
			.body(CustomerResponse.from(customer));
	}

	@GetMapping
	public List<CustomerResponse> list() {
		return listCustomersService.list().stream().map(CustomerResponse::from).toList();
	}

	@GetMapping("/{id}")
	public CustomerResponse get(@PathVariable Long id) {
		return CustomerResponse.from(getCustomerService.get(id));
	}
}
