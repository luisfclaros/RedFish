package com.RedFish.RedFish.orders.interfaces.rest;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.application.service.CreateOrderService;
import com.RedFish.RedFish.orders.application.service.GetOrderService;
import com.RedFish.RedFish.orders.application.service.UpdateOrderStatusService;
import com.RedFish.RedFish.orders.domain.model.Order;
import com.RedFish.RedFish.orders.domain.model.OrderItem;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final CreateOrderService create;
    private final GetOrderService get;
    private final UpdateOrderStatusService update;
    private final CustomerRepositoryPort customers;

    public OrderController(CreateOrderService create, GetOrderService get,
            UpdateOrderStatusService update, CustomerRepositoryPort customers) {
        this.create=create; this.get=get; this.update=update; this.customers=customers;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        var customer=customers.findById(request.customerId())
                .orElseThrow(() -> new IllegalArgumentException("customer not found"));
        var items=request.items().stream().map(i -> new OrderItem(i.productId(), i.productName(),
                i.quantity(), i.unitPrice())).toList();
        Order order=create.create(new Order(null, customer, request.date(), items, request.observations()));
        return ResponseEntity.created(URI.create("/api/v1/orders/"+order.id())).body(OrderResponse.from(order));
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) { return OrderResponse.from(get.get(id)); }

    @PostMapping("/{id}/in-preparation")
    public OrderResponse markInPreparation(@PathVariable Long id) {
        return OrderResponse.from(update.markInPreparation(id));
    }

    @PostMapping("/{id}/dispatched")
    public OrderResponse markDispatched(@PathVariable Long id) {
        return OrderResponse.from(update.markDispatched(id));
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id) {
        return OrderResponse.from(update.cancel(id));
    }
}
