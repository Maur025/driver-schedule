package com.kernotec.driverschedule.service.resource.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.resource.jpa.service.VehicleTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleTypeUpdateCmd extends
    AbstractTransactionalRequiredCommand<VehicleTypeUpdateCmd.Request, UUID>
{

    private final VehicleTypeService vehicleTypeService;

    @Override
    protected UUID run(Request request) {
        var vehicleType = vehicleTypeService.findByIdThrow(request.vehicleTypeId);

        if (request.name != null) {
            vehicleType.setName(request.name);
        }

        vehicleTypeService.save(vehicleType);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleTypeId, String name) {

    }
}
