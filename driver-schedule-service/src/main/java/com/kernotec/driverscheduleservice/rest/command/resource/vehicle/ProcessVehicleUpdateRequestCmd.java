package com.kernotec.driverscheduleservice.rest.command.resource.vehicle;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.resource.vehicle.VehicleUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.resource.VehicleService;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.vehicle.VehicleUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.vehicle.VehicleResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private final VehicleService vehicleService;
    private final WebSocketHandler webSocketHandler;
    private final VehicleResponseMapper vehicleResponseMapper;

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
                          @NotNull VehicleUpdateRequest vehicleUpdateRequest)
    {

    }
}
