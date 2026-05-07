package com.kernotec.driverschedule.service.rest.socket.resource;

import com.kernotec.driverschedule.service.jpa.entity.resource.Location;
import com.kernotec.driverschedule.service.jpa.service.resource.LocationService;
import com.kernotec.driverschedule.service.rest.dto.resource.response.location.LocationResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.location.LocationResponseMapper;
import com.kernotec.driverschedule.service.rest.socket.SocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class LocationSocketHandler extends
    SocketHandler<LocationSocketHandler.Request, LocationResponse>
{

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
