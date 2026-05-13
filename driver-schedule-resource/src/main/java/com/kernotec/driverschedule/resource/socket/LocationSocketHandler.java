package com.kernotec.driverschedule.resource.socket;

import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.resource.rest.dto.response.LocationResponse;
import com.kernotec.driverschedule.resource.rest.mapper.response.LocationResponseMapper;
import com.kernotec.driverschedule.resource.socket.LocationSocketHandler.Request;
import com.kernotec.driverschedule.socket.service.SocketHandler;
import com.kernotec.driverschedule.socket.service.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class LocationSocketHandler extends SocketHandler<Request, LocationResponse> {

    private final LocationService locationService;
    private final LocationResponseMapper locationResponseMapper;

    public LocationSocketHandler(WebSocketHandler webSocketHandler, LocationService locationService,
        LocationResponseMapper locationResponseMapper)
    {
        super(webSocketHandler);
        this.locationService = locationService;
        this.locationResponseMapper = locationResponseMapper;
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
    protected LocationResponse getResponseData(Request request) {
        Location location = locationService.findByIdThrow(request.locationId());
        return locationResponseMapper.toResponse(location);
    }

    @Builder
    public record Request(@NotNull UUID locationId, @NotNull String topic, Set<UUID> toList) {

    }
}
