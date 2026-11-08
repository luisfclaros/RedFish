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
    public PondController(CreatePondService create,ListPondsService list,GetPondService get,PondRepositoryPort repository){
        this.create=create;this.list=list;this.get=get;this.repository=repository;
    }
    @PostMapping
    public ResponseEntity<PondResponse> create(@Valid @RequestBody PondRequest request){
        Pond p=create.create(new Pond(null,request.code(),request.name(),request.location(),request.capacity(),true));
        return ResponseEntity.created(URI.create("/api/v1/ponds/"+p.id())).body(PondResponse.from(p));
    }
    @PutMapping("/{id}")
    public PondResponse update(@PathVariable Long id,@Valid @RequestBody PondRequest request){
        Pond old=get.get(id);
        boolean duplicate=repository.findAll().stream().anyMatch(p->!p.id().equals(id)&&p.code().equalsIgnoreCase(request.code()));
        if(duplicate)throw new IllegalArgumentException("pond code already exists");
        return PondResponse.from(repository.save(new Pond(id,request.code(),request.name(),request.location(),request.capacity(),old.active())));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        Pond p=get.get(id);repository.save(new Pond(id,p.code(),p.name(),p.location(),p.capacity(),false));
        return ResponseEntity.noContent().build();
    }
    @GetMapping public List<PondResponse> list(){return list.list().stream().map(PondResponse::from).toList();}
    @GetMapping("/{id}") public PondResponse get(@PathVariable Long id){return PondResponse.from(get.get(id));}
}