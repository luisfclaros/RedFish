package com.RedFish.RedFish.dispatches.interfaces.rest;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.RedFish.RedFish.dispatches.application.service.GetDispatchService;
import com.RedFish.RedFish.dispatches.application.service.ListDispatchesService;
import com.RedFish.RedFish.dispatches.application.service.ScheduleDispatchService;
import com.RedFish.RedFish.dispatches.application.service.UpdateDispatchStatusService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/dispatches")
public class DispatchController {
    private final ScheduleDispatchService schedule;
    private final ListDispatchesService list;
    private final GetDispatchService get;
    private final UpdateDispatchStatusService update;

    public DispatchController(ScheduleDispatchService schedule, ListDispatchesService list,
            GetDispatchService get, UpdateDispatchStatusService update) {
        this.schedule=schedule; this.list=list; this.get=get; this.update=update;
    }

    @PostMapping
    public ResponseEntity<DispatchResponse> schedule(@Valid @RequestBody ScheduleDispatchRequest request) {
        var dispatch=schedule.schedule(request.orderId(), request.vehicleId(), request.dispatchDate(),
                request.deliveryAddress());
        return ResponseEntity.created(URI.create("/api/v1/dispatches/"+dispatch.id()))
                .body(DispatchResponse.from(dispatch));
    }

    @GetMapping
    public List<DispatchResponse> list() {
        return list.list().stream().map(DispatchResponse::from).toList();
    }

    @GetMapping("/{id}")
    public DispatchResponse get(@PathVariable Long id) { return DispatchResponse.from(get.get(id)); }

    @PostMapping("/{id}/in-transit")
    public DispatchResponse markInTransit(@PathVariable Long id) {
        return DispatchResponse.from(update.markInTransit(id));
    }

    @PostMapping("/{id}/delivered")
    public DispatchResponse markDelivered(@PathVariable Long id,
            @Valid @RequestBody DeliverDispatchRequest request) {
        return DispatchResponse.from(update.markDelivered(id, request.deliveryDate(), request.receivedBy()));
    }

    @PostMapping("/{id}/cancel")
    public DispatchResponse cancel(@PathVariable Long id) {
        return DispatchResponse.from(update.cancel(id));
    }
}
