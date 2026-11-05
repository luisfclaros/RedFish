package com.RedFish.RedFish.modules.infrastructure.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.JpaProductRepository;
import com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.ProductJpaEntity;
import com.RedFish.RedFish.inventory.infrastructure.persistence.jpa.SpringDataProductJpaRepository;
import com.RedFish.RedFish.orders.domain.model.Customer;
import com.RedFish.RedFish.orders.infrastructure.persistence.jpa.CustomerJpaEntity;
import com.RedFish.RedFish.orders.infrastructure.persistence.jpa.JpaCustomerRepository;
import com.RedFish.RedFish.orders.infrastructure.persistence.jpa.SpringDataCustomerJpaRepository;

class JpaPersistenceAdapterTests {

    @Test
    void productEntityMapsToAndFromDomain() {
        Product product = new Product(11L, "PROD-001", "Tilapia Roja", "kg", ProductType.PRODUCT, true);

        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        Product restored = entity.toDomain();

        assertThat(entity.getId()).isEqualTo(11L);
        assertThat(entity.getCode()).isEqualTo("PROD-001");
        assertThat(restored.id()).isEqualTo(product.id());
        assertThat(restored.code()).isEqualTo(product.code());
        assertThat(restored.name()).isEqualTo(product.name());
        assertThat(restored.unitOfMeasure()).isEqualTo(product.unitOfMeasure());
        assertThat(restored.type()).isEqualTo(product.type());
        assertThat(restored.active()).isEqualTo(product.active());
    }

    @Test
    void productJpaAdapterDelegatesSaveQueriesAndExistenceCheck() {
        SpringDataProductJpaRepository springRepository = mock(SpringDataProductJpaRepository.class);
        JpaProductRepository repository = new JpaProductRepository(springRepository);
        Product product = new Product(12L, "INS-001", "Alimento", "kg", ProductType.INPUT, true);
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);

        when(springRepository.save(any(ProductJpaEntity.class))).thenReturn(entity);
        when(springRepository.findAll()).thenReturn(List.of(entity));
        when(springRepository.findById(12L)).thenReturn(Optional.of(entity));
        when(springRepository.existsByCodeIgnoreCase("INS-001")).thenReturn(true);

        assertThat(repository.save(product).code()).isEqualTo("INS-001");
        assertThat(repository.findAll()).extracting(Product::code).containsExactly("INS-001");
        assertThat(repository.findById(12L)).contains(product);
        assertThat(repository.existsByCode("INS-001")).isTrue();

        verify(springRepository).save(any(ProductJpaEntity.class));
        verify(springRepository).findAll();
        verify(springRepository).findById(12L);
        verify(springRepository).existsByCodeIgnoreCase("INS-001");
    }

    @Test
    void customerEntityMapsToAndFromDomain() {
        Customer customer = new Customer(21L, "Piscicola Norte", "3001234567",
            "Via principal", "contacto@example.com", true);

        CustomerJpaEntity entity = CustomerJpaEntity.fromDomain(customer);
        Customer restored = entity.toDomain();

        assertThat(entity.getId()).isEqualTo(21L);
        assertThat(restored.id()).isEqualTo(customer.id());
        assertThat(restored.name()).isEqualTo(customer.name());
        assertThat(restored.phone()).isEqualTo(customer.phone());
        assertThat(restored.address()).isEqualTo(customer.address());
        assertThat(restored.email()).isEqualTo(customer.email());
        assertThat(restored.active()).isEqualTo(customer.active());
    }

    @Test
    void customerJpaAdapterDelegatesSaveQueriesAndExistenceCheck() {
        SpringDataCustomerJpaRepository springRepository = mock(SpringDataCustomerJpaRepository.class);
        JpaCustomerRepository repository = new JpaCustomerRepository(springRepository);
        Customer customer = new Customer(22L, "Piscicola Sur", "3007654321",
            "Vereda Central", "sur@example.com", true);
        CustomerJpaEntity entity = CustomerJpaEntity.fromDomain(customer);

        when(springRepository.save(any(CustomerJpaEntity.class))).thenReturn(entity);
        when(springRepository.findAll()).thenReturn(List.of(entity));
        when(springRepository.findById(22L)).thenReturn(Optional.of(entity));
        when(springRepository.existsByEmailIgnoreCase("sur@example.com")).thenReturn(true);

        assertThat(repository.save(customer).email()).isEqualTo("sur@example.com");
        assertThat(repository.findAll()).extracting(Customer::email).containsExactly("sur@example.com");
        assertThat(repository.findById(22L)).contains(customer);
        assertThat(repository.existsByEmail("sur@example.com")).isTrue();

        verify(springRepository).save(any(CustomerJpaEntity.class));
        verify(springRepository).findAll();
        verify(springRepository).findById(22L);
        verify(springRepository).existsByEmailIgnoreCase("sur@example.com");
    }

    @Test
    void adaptersReturnEmptyWhenEntityIsNotFound() {
        SpringDataProductJpaRepository productSpringRepository = mock(SpringDataProductJpaRepository.class);
        SpringDataCustomerJpaRepository customerSpringRepository = mock(SpringDataCustomerJpaRepository.class);
        when(productSpringRepository.findById(404L)).thenReturn(Optional.empty());
        when(customerSpringRepository.findById(404L)).thenReturn(Optional.empty());

        assertThat(new JpaProductRepository(productSpringRepository).findById(404L)).isEmpty();
        assertThat(new JpaCustomerRepository(customerSpringRepository).findById(404L)).isEmpty();
    }
}
