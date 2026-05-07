package com.kernotec.driverschedule.service.command.resource.vehicle.type;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.resource.VehicleType;
import com.kernotec.driverschedule.service.jpa.service.resource.VehicleTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleTypeCreateCmd extends
    AbstractTransactionalRequiredCommand<VehicleTypeCreateCmd.Request, UUID>
{

    private final VehicleTypeService vehicleTypeService;

    @Override
    protected UUID run(Request request) {
        var vehicleType = new VehicleType();

        vehicleType.setName(request.name);
        vehicleType.setCode(request.code);

        vehicleType = vehicleTypeService.save(vehicleType);
        return vehicleType.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String code) {

    }
}
