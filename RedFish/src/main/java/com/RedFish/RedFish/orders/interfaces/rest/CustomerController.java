package com.RedFish.RedFish.orders.interfaces.rest;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.orders.application.port.CustomerRepositoryPort;
import com.RedFish.RedFish.orders.application.service.CreateCustomerService;
import com.RedFish.RedFish.orders.application.service.GetCustomerService;
import com.RedFish.RedFish.orders.application.service.ListCustomersService;
import com.RedFish.RedFish.orders.domain.model.Customer;
import jakarta.validation.Valid;

@RestController
@RequestMapping({ "/api/v1/customers", "/api/customers" })
public class CustomerController {
    private final CreateCustomerService create;
    private final ListCustomersService list;
    private final GetCustomerService get;
    private final CustomerRepositoryPort repository;

    public CustomerController(CreateCustomerService create, ListCustomersService list,
            GetCustomerService get, CustomerRepositoryPort repository) {
        this.create=create; this.list=list; this.get=get; this.repository=repository;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        Customer customer=create.create(new Customer(null,request.name(),request.phone(),request.address(),request.email(),true));
        return ResponseEntity.created(URI.create("/api/v1/customers/"+customer.id())).body(CustomerResponse.from(customer));
    }
    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id,@Valid @RequestBody CustomerRequest request) {
        Customer old=get.get(id);
        boolean duplicate=request.email()!=null&&!request.email().isBlank()&&repository.findAll().stream()
            .anyMatch(c->!c.id().equals(id)&&request.email().equalsIgnoreCase(c.email()));
        if(duplicate) throw new IllegalArgumentException("customer email already exists");
        return CustomerResponse.from(repository.save(new Customer(id,request.name(),request.phone(),request.address(),request.email(),old.active())));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        Customer c=get.get(id);
        repository.save(new Customer(id,c.name(),c.phone(),c.address(),c.email(),false));
        return ResponseEntity.noContent().build();
    }
    @GetMapping public List<CustomerResponse> list(){return list.list().stream().map(CustomerResponse::from).toList();}
    @GetMapping("/{id}") public CustomerResponse get(@PathVariable Long id){return CustomerResponse.from(this.get.get(id));}
}