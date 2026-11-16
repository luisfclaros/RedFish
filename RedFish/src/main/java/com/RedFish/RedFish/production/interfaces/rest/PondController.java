package com.RedFish.RedFish.production.interfaces.rest;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.production.application.port.PondRepositoryPort;
import com.RedFish.RedFish.production.application.service.CreatePondService;
import com.RedFish.RedFish.production.application.service.GetPondService;
import com.RedFish.RedFish.production.application.service.ListPondsService;
import com.RedFish.RedFish.production.domain.model.Pond;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/ponds")
public class PondController {
    private final CreatePondService create;
    private final ListPondsService list;
    private final GetPondService get;
    private final PondRepositoryPort repository;
    private final com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataPondJpaRepository pondJpa;
    private final com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataFeedingRecordJpaRepository feedingJpa;
    public PondController(CreatePondService create,ListPondsService list,GetPondService get,PondRepositoryPort repository,
            com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataPondJpaRepository pondJpa,
            com.RedFish.RedFish.production.infrastructure.persistence.jpa.SpringDataFeedingRecordJpaRepository feedingJpa){
        this.create=create;this.list=list;this.get=get;this.repository=repository;
        this.pondJpa=pondJpa;this.feedingJpa=feedingJpa;
    }
    @PostMapping
    public ResponseEntity<PondResponse> create(@Valid @RequestBody PondRequest request){
        Pond p=create.create(new Pond(null,request.code(),request.name(),request.location(),request.capacity(),true));
        return ResponseEntity.created(URI.create("/api/v1/ponds/"+p.id())).body(PondResponse.from(p));
    }
    @PutMapping("/{id}")
    public PondResponse update(@PathVariable Long id,@Valid @RequestBody PondRequest request){
        Pond old=get.get(id);
        Pond updated=new Pond(id,request.code(),request.name(),request.location(),request.capacity(),old.active());
        boolean duplicate=repository.findAll().stream()
                .anyMatch(p->!p.id().equals(id)&&p.code().equalsIgnoreCase(updated.code()));
        if(duplicate)throw new IllegalArgumentException("pond code already exists");
        return PondResponse.from(repository.save(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        Pond p=get.get(id);repository.save(new Pond(id,p.code(),p.name(),p.location(),p.capacity(),false));
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> deletePermanently(@PathVariable Long id){
        get.get(id);
        if(feedingJpa.existsByPondId(id))throw new IllegalStateException("No se puede eliminar el estanque porque tiene registros de alimentación asociados. Puedes desactivarlo para conservar el historial.");
        pondJpa.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping public List<PondResponse> list(){return list.list().stream().map(PondResponse::from).toList();}
    @GetMapping("/{id}") public PondResponse get(@PathVariable Long id){return PondResponse.from(get.get(id));}
}
