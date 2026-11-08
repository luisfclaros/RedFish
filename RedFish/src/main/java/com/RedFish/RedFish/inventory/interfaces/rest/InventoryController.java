package com.RedFish.RedFish.inventory.interfaces.rest;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementPort;
import com.RedFish.RedFish.inventory.application.port.InventoryMovementRepositoryPort;
import com.RedFish.RedFish.inventory.application.service.ListInventoryService;
import com.RedFish.RedFish.inventory.application.service.RegisterInventoryStockService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final RegisterInventoryStockService registerStock;
    private final ListInventoryService list;
    private final InventoryMovementPort movementPort;
    private final InventoryMovementRepositoryPort movements;

    public InventoryController(RegisterInventoryStockService registerStock, ListInventoryService list,
            InventoryMovementPort movementPort, InventoryMovementRepositoryPort movements) {
        this.registerStock=registerStock; this.list=list; this.movementPort=movementPort; this.movements=movements;
    }

    @PostMapping("/stock")
    public ResponseEntity<InventoryItemResponse> registerStock(@Valid @RequestBody InventoryStockRequest request) {
        var item=registerStock.register(request.productId(), request.initialQuantity(), request.minimumStock());
        return ResponseEntity.created(URI.create("/api/v1/inventory/"+item.id()))
                .body(InventoryItemResponse.from(item));
    }

    @GetMapping
    public List<InventoryItemResponse> list() {
        return list.list().stream().map(InventoryItemResponse::from).toList();
    }

    @PostMapping("/entries")
    public ResponseEntity<Void> entry(@Valid @RequestBody InventoryMovementRequest request) {
        movementPort.registerEntry(request.productId(), request.quantity(), request.registeredBy(), request.observations());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exits")
    public ResponseEntity<Void> exit(@Valid @RequestBody InventoryMovementRequest request) {
        movementPort.registerExit(request.productId(), request.quantity(), request.registeredBy(), request.observations());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movements")
    public List<InventoryMovementResponse> movements() {
        return movements.findAll().stream().map(InventoryMovementResponse::from).toList();
    }
}
