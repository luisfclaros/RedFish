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
import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.application.port.FeedingRecordRepositoryPort;
import com.RedFish.RedFish.production.domain.model.Pond;
import com.RedFish.RedFish.production.domain.model.FeedingRecord;
import java.time.LocalDate;
import java.math.BigDecimal;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class RedFishApplicationTests {

    @Autowired
    private ProductRepositoryPort productRepository;

    @Autowired
    private CustomerRepositoryPort customerRepository;

    @Autowired
    private VehicleRepositoryPort vehicleRepository;

    @Autowired
    private PondRepositoryPort pondRepository;

    @Autowired
    private FeedingRecordRepositoryPort feedingRecordRepository;

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
    void persistsAndReadsVehicleFromMysql() {
        String plate = "IT" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Vehicle saved = vehicleRepository.save(
            new Vehicle(null, plate, "Chevrolet", "NPR", new BigDecimal("3500"), true, "Prueba"));

        assertThat(saved.id()).isNotNull();
        assertThat(vehicleRepository.findById(saved.id())).get().extracting(Vehicle::plate).isEqualTo(plate);
        assertThat(vehicleRepository.existsByPlate(plate)).isTrue();
    }

    @Test
    void persistsAndReadsPondFromMysql() {
        String code = "P-" + UUID.randomUUID().toString().substring(0, 8);
        Pond saved = pondRepository.save(new Pond(null, code, "Estanque de prueba", "Neiva",
                new BigDecimal("120"), true));
        assertThat(saved.id()).isNotNull();
        assertThat(pondRepository.findById(saved.id())).get().extracting(Pond::code).isEqualTo(code);
        assertThat(pondRepository.existsByCode(code)).isTrue();
    }

    @Test
    void persistsAndReadsFeedingRecordFromMysql() {
        String code = "F-" + UUID.randomUUID().toString().substring(0, 8);
        Pond pond = pondRepository.save(new Pond(null, code, "Estanque alimento", "Neiva",
                new BigDecimal("100"), true));
        FeedingRecord saved = feedingRecordRepository.save(new FeedingRecord(null, pond.id(),
                LocalDate.now(), "Concentrado", new BigDecimal("5.5"), 1L, "Turno mañana"));
        assertThat(saved.id()).isNotNull();
        assertThat(feedingRecordRepository.findByPondId(pond.id())).hasSize(1)
                .first().extracting(FeedingRecord::foodType).isEqualTo("Concentrado");
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
