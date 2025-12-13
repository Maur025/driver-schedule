package com.kernotec.driverscheduleservice.command.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleUpdateCmd extends
    AbstractTransactionalRequiredCommand<VehicleUpdateCmd.Request, Void>
{

    private final VehicleService vehicleService;

    @Override
    protected Void run(Request request) {
        Vehicle vehicle = vehicleService.findByIdThrow(request.vehicleId);

        if (request.vehicleNumber != null) {
            vehicle.setVehicleNumber(request.vehicleNumber);
        }
        if (request.model != null) {
            vehicle.setModel(request.model);
        }
        if (request.capacity != null) {
            vehicle.setCapacity(request.capacity);
        }

        vehicleService.save(vehicle);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleId, String vehicleNumber, String model,
                          Integer capacity)
    {

    }
}
