package com.RedFish.RedFish.orders.infrastructure.persistence.jpa;

import com.RedFish.RedFish.orders.domain.model.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class CustomerJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 30)
	private String phone;

	@Column(nullable = false, length = 150)
	private String address;

	@Column(unique = true, length = 120)
	private String email;

	@Column(nullable = false)
	private boolean active;

	protected CustomerJpaEntity() {
	}

	private CustomerJpaEntity(Long id, String name, String phone, String address, String email, boolean active) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.active = active;
	}

	public static CustomerJpaEntity fromDomain(Customer customer) {
		return new CustomerJpaEntity(customer.id(), customer.name(), customer.phone(), customer.address(),
				customer.email(), customer.active());
	}

	public Customer toDomain() {
		return new Customer(id, name, phone, address, email, active);
	}

	public Long getId() {
		return id;
	}
}
