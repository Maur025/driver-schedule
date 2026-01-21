package com.kernotec.driverscheduleservice.rest.command.vehicle;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.vehicle.VehicleUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.VehiclePatchRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.vehicle.VehicleResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessVehiclePatchRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessVehiclePatchRequestCmd.Request, Void>
{

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;

    private final VehicleUpdateCmd vehicleUpdateCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected Void run(Request request) {
        VehiclePatchRequest vehiclePatchRequest = request.vehiclePatchRequest;

        vehicleUpdateCmd.withRequest(VehicleUpdateCmd.Request.builder()
                .vehicleId(request.vehicleId)
                .isEnabled(vehiclePatchRequest.getIsEnabled())
                .build())
            .execute();

        Vehicle vehicle = vehicleService.findByIdThrow(request.vehicleId);

        webSocketHandler.emitMessage(
            WebSocketTopic.VEHICLE_UPDATED, WebSocketSingleResponse.<VehicleResponse>builder()
                .topic(WebSocketTopic.VEHICLE_UPDATED)
                .timestamp(ZonedDateTime.now())
                .data(vehicleResponseMapper.toResponse(vehicle))
                .build()
        );

        return null;
    }

    @Builder
    public record Request(@NotNull UUID vehicleId,
                          @NotNull @Valid VehiclePatchRequest vehiclePatchRequest)
    {

    }
}
