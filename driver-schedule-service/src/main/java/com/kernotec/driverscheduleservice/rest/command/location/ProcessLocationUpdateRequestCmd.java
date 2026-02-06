package com.kernotec.driverscheduleservice.rest.command.location;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.location.LocationUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.request.location.LocationUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.location.LocationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.location.LocationResponseMapper;
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
public class ProcessLocationUpdateRequestCmd extends
    AbstractCommand<ProcessLocationUpdateRequestCmd.Request, Void>
{

    private final LocationService locationService;
    private final LocationResponseMapper locationResponseMapper;

    private final LocationUpdateCmd locationUpdateCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected Void run(Request request) {
        LocationUpdateRequest locationUpdateRequest = request.locationUpdateRequest;

        Coordinate coordinate = null;

        if (locationUpdateRequest.getCoordinates() != null) {
            coordinate = locationService.getCoordinateOfList(
                locationUpdateRequest.getCoordinates());
        }

        locationUpdateCmd.withRequest(LocationUpdateCmd.Request.builder()
                .locationId(request.locationId)
                .name(locationUpdateRequest.getName())
                .description(locationUpdateRequest.getDescription())
                .coordinate(coordinate)
                .icon(locationUpdateRequest.getIcon())
                .color(locationUpdateRequest.getColor())
                .placeCategoryId(locationUpdateRequest.getPlaceCategoryId())
                .build())
            .execute();

        Location location = locationService.findByIdThrow(request.locationId);

        webSocketHandler.emitMessage(
            WebSocketTopic.LOCATION_UPDATED, WebSocketSingleResponse.<LocationResponse>builder()
                .topic(WebSocketTopic.LOCATION_UPDATED)
                .timestamp(ZonedDateTime.now())
                .data(locationResponseMapper.toResponse(location))
                .build()
        );

        return null;
    }

    @Builder
    public record Request(@NotNull UUID locationId,
                          @NotNull LocationUpdateRequest locationUpdateRequest)
    {

    }
}
