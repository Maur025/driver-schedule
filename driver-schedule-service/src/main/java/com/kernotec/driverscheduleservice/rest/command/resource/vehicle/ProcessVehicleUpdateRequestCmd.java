package com.kernotec.driverscheduleservice.rest.command.resource.vehicle;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.resource.vehicle.VehicleUpdateCmd;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.vehicle.VehicleUpdateRequest;
import com.kernotec.driverscheduleservice.rest.socket.resource.VehicleSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
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
