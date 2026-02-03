package com.kernotec.driverscheduleservice.rest.command.location;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.location.LocationCreateCmd;
import com.kernotec.driverscheduleservice.exception.LocationException;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.request.location.LocationCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.location.LocationResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessLocationCreateRequestCmd extends
    AbstractCommand<ProcessLocationCreateRequestCmd.Request, UUID>
{

    private final LocationCreateCmd locationCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final LocationResponseMapper locationResponseMapper;
    private final LocationService locationService;

    @Override
    protected void validate(Request request) {
        LocationCreateRequest locationCreateRequest = request.locationCreateRequest;

        if (locationCreateRequest.getCoordinates()
            .isEmpty())
        {
            throw new LocationException("coordinate.empty", "", HttpStatus.BAD_REQUEST.value());
        }
    }

    @Override
    protected UUID run(Request request) {
        LocationCreateRequest locationCreateRequest = request.locationCreateRequest;

        Coordinate coordinate = locationService.getCoordinateOfList(
            locationCreateRequest.getCoordinates());

        UUID locationId = locationCreateCmd.withRequest(LocationCreateCmd.Request.builder()
                .name(locationCreateRequest.getName())
                .description(locationCreateRequest.getDescription())
                .coordinate(coordinate)
                .icon(locationCreateRequest.getIcon())
                .color(locationCreateRequest.getColor())
                .placeCategoryId(locationCreateRequest.getPlaceCategoryId())
                .build())
            .execute();

        Location location = locationService.findByIdThrow(locationId);

        webSocketHandler.emitMessage(
            WebSocketTopic.LOCATION_CREATED, WebSocketSingleResponse.builder()
                .topic(WebSocketTopic.LOCATION_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(locationResponseMapper.toResponse(location))
                .build()
        );

        return locationId;
    }

    @Builder
    public record Request(@NotNull @Valid LocationCreateRequest locationCreateRequest) {

    }
}
