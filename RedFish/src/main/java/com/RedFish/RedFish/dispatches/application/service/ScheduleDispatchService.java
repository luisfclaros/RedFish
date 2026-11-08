package com.RedFish.RedFish.dispatches.application.service;

import static com.RedFish.RedFish.shared.domain.DomainValidation.requireNonNull;
import static com.RedFish.RedFish.shared.domain.DomainValidation.requireText;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RedFish.RedFish.dispatches.application.port.DispatchRepositoryPort;
import com.RedFish.RedFish.dispatches.application.port.OrderAvailabilityPort;
import com.RedFish.RedFish.dispatches.application.port.VehicleAvailabilityPort;
import com.RedFish.RedFish.dispatches.domain.model.Dispatch;

@Service
public class ScheduleDispatchService {
    private final DispatchRepositoryPort dispatchRepository;
    private final VehicleAvailabilityPort vehicleAvailability;
    private final OrderAvailabilityPort orderAvailability;

    public ScheduleDispatchService(DispatchRepositoryPort dispatchRepository, VehicleAvailabilityPort vehicleAvailability) {
        this(dispatchRepository, vehicleAvailability, orderId -> {});
    }

    @Autowired
    public ScheduleDispatchService(DispatchRepositoryPort dispatchRepository, VehicleAvailabilityPort vehicleAvailability,
            OrderAvailabilityPort orderAvailability) {
        this.dispatchRepository=requireNonNull(dispatchRepository, "dispatch repository");
        this.vehicleAvailability=requireNonNull(vehicleAvailability, "vehicle availability");
        this.orderAvailability=requireNonNull(orderAvailability, "order availability");
    }

    @Transactional
    public Dispatch schedule(Long orderId, Long vehicleId, LocalDate dispatchDate, String deliveryAddress) {
        requireNonNull(orderId, "order id");
        requireNonNull(vehicleId, "vehicle id");
        requireNonNull(dispatchDate, "dispatch date");
        orderAvailability.ensureOrderCanBeDispatched(orderId);
        vehicleAvailability.ensureVehicleCanBeAssigned(vehicleId);
        Dispatch dispatch=new Dispatch(null, orderId, vehicleId, dispatchDate,
                requireText(deliveryAddress, "delivery address"));
        return dispatchRepository.save(dispatch);
    }
}
