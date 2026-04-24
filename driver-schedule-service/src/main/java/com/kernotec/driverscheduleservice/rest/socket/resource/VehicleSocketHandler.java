package com.kernotec.driverscheduleservice.rest.socket.resource;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Vehicle;
import com.kernotec.driverscheduleservice.jpa.service.resource.VehicleService;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.VehicleResponseMapper;
import com.kernotec.driverscheduleservice.rest.socket.SocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class VehicleSocketHandler extends
    SocketHandler<VehicleSocketHandler.Request, VehicleResponse>
{

    private final VehicleService vehicleService;
    private final VehicleResponseMapper vehicleResponseMapper;

    public VehicleSocketHandler(WebSocketHandler webSocketHandler, VehicleService vehicleService,
        VehicleResponseMapper vehicleResponseMapper)
    {
        super(webSocketHandler);
        this.vehicleService = vehicleService;
        this.vehicleResponseMapper = vehicleResponseMapper;
    }

    @Override
    protected String getTopic(Request request) {
        return request.topic();
    }

    @Override
    protected Set<UUID> getToList(Request request) {
        return request.toList();
    }

    @Override
    protected VehicleResponse getResponseData(Request request) {
        Vehicle vehicle = vehicleService.findByIdThrow(request.vehicleId());
        return vehicleResponseMapper.toResponse(vehicle);
    }

    @Builder
    public record Request(@NotNull UUID vehicleId, @NotNull String topic, Set<UUID> toList) {

    }
}
