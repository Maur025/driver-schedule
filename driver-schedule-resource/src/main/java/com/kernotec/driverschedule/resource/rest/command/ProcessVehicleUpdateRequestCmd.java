package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.resource.command.VehicleUpdateCmd;
import com.kernotec.driverschedule.resource.rest.dto.request.VehicleUpdateRequest;
import com.kernotec.driverschedule.resource.socket.ResourceSocketTopic;
import com.kernotec.driverschedule.resource.socket.VehicleSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessVehicleUpdateRequestCmd extends
    AbstractCommand<ProcessVehicleUpdateRequestCmd.Request, Void>
{

    private final VehicleUpdateCmd vehicleUpdateCmd;
    private final VehicleSocketHandler vehicleSocketHandler;

    @Override
    protected Void run(Request request) {
        VehicleUpdateRequest vehicleUpdateRequest = request.vehicleUpdateRequest;

        vehicleUpdateCmd.withRequest(VehicleUpdateCmd.Request.builder()
                .vehicleId(request.vehicleId)
                .vehicleNumber(vehicleUpdateRequest.getVehicleNumber())
                .model(vehicleUpdateRequest.getModel())
                .capacity(vehicleUpdateRequest.getCapacity())
                .vehicleTypeId(vehicleUpdateRequest.getVehicleTypeId())
                .build())
            .execute();

        vehicleSocketHandler.emitMessage(VehicleSocketHandler.Request.builder()
            .vehicleId(request.vehicleId())
            .topic(ResourceSocketTopic.VEHICLE_UPDATED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleId,
                          @NotNull VehicleUpdateRequest vehicleUpdateRequest)
    {

    }
}
