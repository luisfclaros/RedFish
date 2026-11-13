package com.RedFish.RedFish.vehicles.interfaces.rest;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.vehicles.application.port.VehicleRepositoryPort;
import com.RedFish.RedFish.vehicles.application.service.CreateVehicleService;
import com.RedFish.RedFish.vehicles.application.service.GetVehicleService;
import com.RedFish.RedFish.vehicles.application.service.ListVehiclesService;
import com.RedFish.RedFish.vehicles.domain.model.Vehicle;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {
    private final CreateVehicleService create;
    private final ListVehiclesService list;
    private final GetVehicleService get;
    private final VehicleRepositoryPort repository;
    private final com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa.SpringDataVehicleJpaRepository vehicleJpa;
    private final com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa.SpringDataDispatchJpaRepository dispatchJpa;
    public VehicleController(CreateVehicleService create,ListVehiclesService list,GetVehicleService get,VehicleRepositoryPort repository,
            com.RedFish.RedFish.vehicles.infrastructure.persistence.jpa.SpringDataVehicleJpaRepository vehicleJpa,
            com.RedFish.RedFish.dispatches.infrastructure.persistence.jpa.SpringDataDispatchJpaRepository dispatchJpa){
        this.create=create;this.list=list;this.get=get;this.repository=repository;
        this.vehicleJpa=vehicleJpa;this.dispatchJpa=dispatchJpa;
    }
    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request){
        Vehicle v=create.create(new Vehicle(null,request.plate(),request.brand(),request.model(),request.loadCapacity(),true,request.observations()));
        return ResponseEntity.created(URI.create("/api/v1/vehicles/"+v.id())).body(VehicleResponse.from(v));
    }
    @PutMapping("/{id}")
    public VehicleResponse update(@PathVariable Long id,@Valid @RequestBody VehicleRequest request){
        Vehicle old=get.get(id);
        boolean duplicate=repository.findAll().stream().anyMatch(v->!v.id().equals(id)&&v.plate().equalsIgnoreCase(request.plate()));
        if(duplicate)throw new IllegalArgumentException("vehicle plate already exists");
        return VehicleResponse.from(repository.save(new Vehicle(id,request.plate(),request.brand(),request.model(),request.loadCapacity(),old.active(),request.observations())));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        Vehicle v=get.get(id);repository.save(new Vehicle(id,v.plate(),v.brand(),v.model(),v.loadCapacity(),false,v.observations()));
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> deletePermanently(@PathVariable Long id){
        get.get(id);
        if(dispatchJpa.existsByVehicleId(id))throw new IllegalStateException("No se puede eliminar el vehículo porque tiene despachos asociados. Puedes desactivarlo para conservar el historial.");
        vehicleJpa.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping public List<VehicleResponse> list(){return list.list().stream().map(VehicleResponse::from).toList();}
    @GetMapping("/{id}") public VehicleResponse get(@PathVariable Long id){return VehicleResponse.from(get.get(id));}
}