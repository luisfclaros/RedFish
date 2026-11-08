package com.RedFish.RedFish.production.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public PondController(CreatePondService create, ListPondsService list, GetPondService get) {
        this.create=create; this.list=list; this.get=get;
    }

    @PostMapping
    public ResponseEntity<PondResponse> create(@Valid @RequestBody PondRequest request) {
        Pond pond = create.create(new Pond(null, request.code(), request.name(), request.location(),
                request.capacity(), true));
        return ResponseEntity.created(URI.create("/api/v1/ponds/" + pond.id())).body(PondResponse.from(pond));
    }
    @GetMapping public List<PondResponse> list() { return list.list().stream().map(PondResponse::from).toList(); }
    @GetMapping("/{id}") public PondResponse get(@PathVariable Long id) { return PondResponse.from(get.get(id)); }
}
