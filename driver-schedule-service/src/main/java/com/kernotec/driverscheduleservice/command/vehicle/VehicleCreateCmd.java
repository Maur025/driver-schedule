package com.kernotec.driverscheduleservice.command.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleCreateCmd extends
    AbstractTransactionalRequiredCommand<VehicleCreateCmd.Request, UUID>
{

    private final VehicleService vehicleService;

    @Override
    protected UUID run(Request request) {
        var vehicle = new Vehicle();

        vehicle.setVehicleNumber(request.vehicleNumber);
        vehicle.setVehicleTypeId(request.vehicleTypeId);
        vehicle.setModel(request.model);
        vehicle.setCapacity(request.capacity);
        vehicle.setEnabled(request.isEnabled() != null && request.isEnabled());

        vehicle = vehicleService.save(vehicle);
        return vehicle.getId();
    }

    @Builder
    public record Request(@NotNull @NotBlank String vehicleNumber, @NotNull UUID vehicleTypeId,
                          String model, Integer capacity, Boolean isEnabled)
    {

    }
}
