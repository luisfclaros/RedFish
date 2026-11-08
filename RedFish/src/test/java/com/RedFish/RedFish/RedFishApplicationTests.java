package com.RedFish.RedFish;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.core.io.ClassPathResource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
import com.RedFish.RedFish.inventory.application.port.InventoryRepositoryPort;
import com.RedFish.RedFish.inventory.domain.model.InventoryItem;
import com.RedFish.RedFish.orders.application.port.OrderRepositoryPort;
import com.RedFish.RedFish.orders.application.service.CreateOrderService;
import com.RedFish.RedFish.orders.application.service.UpdateOrderStatusService;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.application.service.ScheduleDispatchService;
import com.RedFish.RedFish.dispatches.application.service.UpdateDispatchStatusService;
import com.RedFish.RedFish.dispatches.domain.model.DispatchStatus;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class RedFishApplicationTests {

    @Autowired
    private MockMvc mockMvc;

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

    @Autowired
    private InventoryRepositoryPort inventoryRepository;

    @Autowired
    private OrderRepositoryPort orderRepository;

    @Autowired
    private CreateOrderService createOrderService;

    @Autowired
    private UpdateOrderStatusService updateOrderStatusService;

    @Autowired
    private DispatchRepositoryPort dispatchRepository;

    @Autowired
    private ScheduleDispatchService scheduleDispatchService;

    @Autowired
    private UpdateDispatchStatusService updateDispatchStatusService;

    @Test
    void servesBrowserDashboardWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        assertThat(new ClassPathResource("static/index.html").exists()).isTrue();
    }

    @Test
    void protectsBusinessApiFromAnonymousRequests() throws Exception {
        mockMvc.perform(get("/api/v1/products")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "operator", roles = "OPERATOR")
    void servesBusinessApiToAuthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/v1/products")).andExpect(status().isOk());
    }

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
    void createsOrderAndReservesInventoryInMysql() {
        String productCode = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Product product = productRepository.save(new Product(null, productCode, "Tilapia Roja", "kg", ProductType.PRODUCT, true));
        inventoryRepository.save(new InventoryItem(null, product.id(), new BigDecimal("10"), new BigDecimal("2")));
        String email = UUID.randomUUID() + "@orders.redfish.test";
        Customer customer = customerRepository.save(new Customer(null, "Cliente pedido", "3001234567", "Neiva", email, true));

        Order created = createOrderService.create(new Order(null, customer, LocalDate.now(),
                java.util.List.of(new OrderItem(product.id(), product.name(), new BigDecimal("3"), new BigDecimal("12000"))),
                "Pedido integración"));

        assertThat(created.id()).isNotNull();
        assertThat(created.total()).isEqualByComparingTo("36000");
        assertThat(orderRepository.findById(created.id())).get().extracting(Order::status).isEqualTo(created.status());
        assertThat(inventoryRepository.findByProductId(product.id())).get()
                .extracting(InventoryItem::quantity).isEqualTo(new BigDecimal("7.000"));
    }

    @Test
    void cancellingOrderRestoresReservedInventoryInMysql() {
        String code = "CAN-" + UUID.randomUUID().toString().substring(0, 8);
        Product product = productRepository.save(new Product(null, code, "Tilapia cancelación", "kg", ProductType.PRODUCT, true));
        inventoryRepository.save(new InventoryItem(null, product.id(), new BigDecimal("10"), new BigDecimal("1")));
        Customer customer = customerRepository.save(new Customer(null, "Cliente cancelación", "3001234567",
                "Neiva", UUID.randomUUID()+"@cancel.redfish.test", true));
        Order order = createOrderService.create(new Order(null, customer, LocalDate.now(),
                java.util.List.of(new OrderItem(product.id(), product.name(), new BigDecimal("4"), new BigDecimal("10000"))), null));
        assertThat(inventoryRepository.findByProductId(product.id())).get()
                .extracting(InventoryItem::quantity).isEqualTo(new BigDecimal("6.000"));

        updateOrderStatusService.cancel(order.id());

        assertThat(inventoryRepository.findByProductId(product.id())).get()
                .extracting(InventoryItem::quantity).isEqualTo(new BigDecimal("10.000"));
    }

    @Test
    void schedulesDispatchAndSynchronizesOrderLifecycleInMysql() {
        String code = "DSP-" + UUID.randomUUID().toString().substring(0, 8);
        Product product = productRepository.save(new Product(null, code, "Tilapia despacho", "kg", ProductType.PRODUCT, true));
        inventoryRepository.save(new InventoryItem(null, product.id(), new BigDecimal("20"), new BigDecimal("2")));
        Customer customer = customerRepository.save(new Customer(null, "Cliente despacho", "3001234567",
                "Neiva", UUID.randomUUID()+"@dispatch.redfish.test", true));
        Order order = createOrderService.create(new Order(null, customer, LocalDate.now(),
                java.util.List.of(new OrderItem(product.id(), product.name(), new BigDecimal("2"), new BigDecimal("10000"))), null));
        order.markInPreparation();
        orderRepository.save(order);
        Vehicle vehicle = vehicleRepository.save(new Vehicle(null,
                "D"+UUID.randomUUID().toString().substring(0, 5).toUpperCase(), "Chevrolet", "NPR",
                new BigDecimal("3500"), true, null));

        var dispatch = scheduleDispatchService.schedule(order.id(), vehicle.id(), LocalDate.now(), "Bodega principal");
        assertThat(dispatchRepository.findById(dispatch.id())).isPresent();
        var inTransit = updateDispatchStatusService.markInTransit(dispatch.id());

        assertThat(inTransit.status()).isEqualTo(DispatchStatus.IN_TRANSIT);
        assertThat(orderRepository.findById(order.id())).get().extracting(Order::status)
                .isEqualTo(com.RedFish.RedFish.orders.domain.model.OrderStatus.DISPATCHED);
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
