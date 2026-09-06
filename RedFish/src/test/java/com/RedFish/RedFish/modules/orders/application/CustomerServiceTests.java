package com.RedFish.RedFish.modules.orders.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.orders.application.service.CreateCustomerService;
import com.RedFish.RedFish.orders.application.service.GetCustomerService;
import com.RedFish.RedFish.orders.application.service.ListCustomersService;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.infrastructure.persistence.inmemory.InMemoryCustomerRepository;

class CustomerServiceTests {

	@Test
	void createsAndListsCustomersUsingRepositoryPort() {
		InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
		CreateCustomerService createCustomerService = new CreateCustomerService(repository);
		ListCustomersService listCustomersService = new ListCustomersService(repository);

		Customer customer = createCustomerService.create(new Customer(null, "Restaurante El Lago", "3001234567",
				"Calle 10 # 15-20", "compras@ellago.com", true));

		assertThat(customer.id()).isNotNull();
		assertThat(listCustomersService.list()).hasSize(1);
	}

	@Test
	void rejectsDuplicatedCustomerEmail() {
		InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
		CreateCustomerService createCustomerService = new CreateCustomerService(repository);
		createCustomerService.create(new Customer(null, "Restaurante El Lago", "3001234567", "Calle 10 # 15-20",
				"compras@ellago.com", true));

		assertThatThrownBy(() -> createCustomerService.create(new Customer(null, "Restaurante Rio Claro", "3007654321",
				"Calle 20 # 8-40", "compras@ellago.com", true)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("already exists");
	}

	@Test
	void getsCustomerById() {
		InMemoryCustomerRepository repository = new InMemoryCustomerRepository();
		CreateCustomerService createCustomerService = new CreateCustomerService(repository);
		GetCustomerService getCustomerService = new GetCustomerService(repository);
		Customer customer = createCustomerService.create(new Customer(null, "Distribuidora Pez Vivo", "3011112233",
				"Carrera 5 # 11-30", "pedidos@pezvivo.com", true));

		assertThat(getCustomerService.get(customer.id()).name()).isEqualTo("Distribuidora Pez Vivo");
	}
}
