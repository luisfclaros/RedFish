package com.RedFish.RedFish.vehicles.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RedFish.RedFish.vehicles.application.service.CreateVehicleService;
import com.RedFish.RedFish.vehicles.application.service.GetVehicleService;
import com.RedFish.RedFish.vehicles.application.service.ListVehiclesService;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final CreateVehicleService createService;
    private final ListVehiclesService listService;
    private final GetVehicleService getService;

    public VehicleController(CreateVehicleService createService, ListVehiclesService listService,
            GetVehicleService getService) {
        this.createService = createService;
        this.listService = listService;
        this.getService = getService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        Vehicle vehicle = createService.create(new Vehicle(null, request.plate(), request.brand(),
                request.model(), request.loadCapacity(), true, request.observations()));
        return ResponseEntity.created(URI.create("/api/v1/vehicles/" + vehicle.id()))
                .body(VehicleResponse.from(vehicle));
    }

    @GetMapping
    public List<VehicleResponse> list() {
        return listService.list().stream().map(VehicleResponse::from).toList();
    }

    @GetMapping("/{id}")
    public VehicleResponse get(@PathVariable Long id) {
        return VehicleResponse.from(getService.get(id));
    }
}
