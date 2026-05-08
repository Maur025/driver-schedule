package com.kernotec.driverschedule.service.rest.socket.resource;

import com.kernotec.driverschedule.service.jpa.entity.resource.Vehicle;
import com.kernotec.driverschedule.service.jpa.service.resource.VehicleService;
import com.kernotec.driverschedule.service.rest.dto.resource.response.vehicle.VehicleResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle.VehicleResponseMapper;
import com.kernotec.driverschedule.service.rest.socket.resource.VehicleSocketHandler.Request;
import com.kernotec.driverschedule.socket.service.SocketHandler;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class VehicleSocketHandler extends SocketHandler<Request, VehicleResponse> {

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
