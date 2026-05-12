package com.kernotec.driverschedule.service.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.resource.command.VehicleCreateCmd;
import com.kernotec.driverschedule.service.resource.rest.dto.request.VehicleCreateRequest;
import com.kernotec.driverschedule.service.resource.socket.VehicleSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessVehicleCreateRequestCmd extends
    AbstractCommand<ProcessVehicleCreateRequestCmd.Request, UUID>
{

    private final VehicleCreateCmd vehicleCreateCmd;
    private final VehicleSocketHandler vehicleSocketHandler;

    @Override
    protected UUID run(Request request) {
        VehicleCreateRequest vehicleCreateRequest = request.vehicleCreateRequest;

        UUID vehicleId = vehicleCreateCmd.withRequest(VehicleCreateCmd.Request.builder()
                .vehicleNumber(vehicleCreateRequest.getVehicleNumber())
                .model(vehicleCreateRequest.getModel())
                .capacity(vehicleCreateRequest.getCapacity())
                .vehicleTypeId(vehicleCreateRequest.getVehicleTypeId())
                .isEnabled(true)
                .build())
            .execute();

        vehicleSocketHandler.emitMessage(VehicleSocketHandler.Request.builder()
            .vehicleId(vehicleId)
            .topic(WebSocketTopic.VEHICLE_CREATED)
            .build());

        return vehicleId;
    }

    @Builder
    public record Request(@NotNull VehicleCreateRequest vehicleCreateRequest) {

    }
}
