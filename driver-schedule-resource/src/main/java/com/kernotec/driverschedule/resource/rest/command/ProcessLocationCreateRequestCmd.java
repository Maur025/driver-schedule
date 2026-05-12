package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.resource.command.LocationCreateCmd;
import com.kernotec.driverschedule.resource.exception.LocationException;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.resource.rest.dto.request.LocationCreateRequest;
import com.kernotec.driverschedule.resource.socket.LocationSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private final LocationService locationService;
    private final LocationSocketHandler locationSocketHandler;

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

        locationSocketHandler.emitMessage(LocationSocketHandler.Request.builder()
            .locationId(locationId)
            .topic(WebSocketTopic.LOCATION_CREATED)
            .build());

        return locationId;
    }

    @Builder
    public record Request(@NotNull @Valid LocationCreateRequest locationCreateRequest) {

    }
}
