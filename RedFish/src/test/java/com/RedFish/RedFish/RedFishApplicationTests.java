package com.RedFish.RedFish;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.RedFish.RedFish.inventory.application.port.ProductRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.Product;
import com.RedFish.RedFish.inventory.domain.model.ProductType;
import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.domain.model.Customer;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RedFishApplicationTests {

    @Autowired
    private ProductRepositoryPort productRepository;

    @Autowired
    private CustomerRepositoryPort customerRepository;

    @Test
    void applicationContextLoadsWithMysqlInfrastructure() {
        assertThat(productRepository).isNotNull();
        assertThat(customerRepository).isNotNull();
    }

    @Test
    void persistsAndReadsProductFromMysql() {
        String code = "IT-" + UUID.randomUUID().toString().substring(0, 8);
        Product saved = productRepository.save(
            new Product(null, code, "Tilapia Roja", "kg", ProductType.PRODUCT, true));

        assertThat(saved.id()).isNotNull();
        assertThat(productRepository.findById(saved.id()))
            .get()
            .extracting(Product::code)
            .isEqualTo(code);
        assertThat(productRepository.existsByCode(code)).isTrue();
    }

    @Test
    void persistsAndReadsCustomerFromMysql() {
        String email = UUID.randomUUID() + "@redfish.test";
        Customer saved = customerRepository.save(
            new Customer(null, "Restaurante de prueba", "3001234567", "Neiva", email, true));

        assertThat(saved.id()).isNotNull();
        assertThat(customerRepository.findById(saved.id()))
            .get()
            .extracting(Customer::email)
            .isEqualTo(email);
        assertThat(customerRepository.existsByEmail(email)).isTrue();
    }
}
