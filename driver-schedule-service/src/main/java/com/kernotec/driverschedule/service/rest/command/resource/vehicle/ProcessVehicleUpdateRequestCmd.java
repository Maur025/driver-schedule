package com.kernotec.driverschedule.service.rest.command.resource.vehicle;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.command.resource.vehicle.VehicleUpdateCmd;
import com.kernotec.driverschedule.service.rest.dto.resource.request.vehicle.VehicleUpdateRequest;
import com.kernotec.driverschedule.service.rest.socket.resource.VehicleSocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketTopic;
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
            .topic(WebSocketTopic.VEHICLE_UPDATED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleId,
                          @NotNull VehicleUpdateRequest vehicleUpdateRequest)
    {

    }
}
