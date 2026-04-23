package com.kernotec.driverscheduleservice.command.resource.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.exception.custom.base.DefaultApiException;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.resource.VehicleService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VehicleCreateManyCmd extends
    AbstractTransactionalRequiredCommand<VehicleCreateManyCmd.Request, List<Vehicle>>
{

    private final VehicleService vehicleService;

    @Override
    protected List<Vehicle> run(Request request) {
        if (request.vehicleList.isEmpty()) {
            throw new DefaultApiException("No vehicles to create");
        }

        return vehicleService.saveAll(request.vehicleList);
    }

    @Builder
    public record Request(@NotNull List<Vehicle> vehicleList) {

    }

}
