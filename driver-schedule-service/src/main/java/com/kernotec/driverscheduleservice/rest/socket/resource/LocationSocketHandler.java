package com.kernotec.driverscheduleservice.rest.socket.resource;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Location;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.location.LocationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.location.LocationResponseMapper;
import com.kernotec.driverscheduleservice.rest.socket.SocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
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
