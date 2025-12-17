package com.kernotec.driverscheduleservice.rest.command.vehicle;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.vehicle.VehicleCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.VehicleService;
import com.kernotec.driverscheduleservice.rest.dto.request.vehicle.VehicleCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.vehicle.VehicleResponseMapper;
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
public class ProcessVehicleCreateRequestCmd extends
    AbstractCommand<ProcessVehicleCreateRequestCmd.Request, UUID>
{

    private final VehicleCreateCmd vehicleCreateCmd;
    private final VehicleService vehicleService;
    private final WebSocketHandler webSocketHandler;
    private final VehicleResponseMapper vehicleResponseMapper;

    @Override
    protected UUID run(Request request) {
        VehicleCreateRequest vehicleCreateRequest = request.vehicleCreateRequest;

        UUID vehicleId = vehicleCreateCmd.withRequest(VehicleCreateCmd.Request.builder()
                .vehicleNumber(vehicleCreateRequest.getVehicleNumber())
                .model(vehicleCreateRequest.getModel())
                .capacity(vehicleCreateRequest.getCapacity())
                .build())
            .execute();

        Vehicle vehicle = vehicleService.findByIdThrow(vehicleId);

        webSocketHandler.emitMessage(
            WebSocketTopic.VEHICLE_CREATED, WebSocketSingleResponse.<VehicleResponse>builder()
                .topic(WebSocketTopic.VEHICLE_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(vehicleResponseMapper.toResponse(vehicle))
                .build()
        );

        return vehicleId;
    }

    @Builder
    public record Request(@NotNull VehicleCreateRequest vehicleCreateRequest) {

    }
}
