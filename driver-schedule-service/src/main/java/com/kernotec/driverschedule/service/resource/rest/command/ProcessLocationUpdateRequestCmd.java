package com.kernotec.driverschedule.service.resource.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.resource.command.LocationUpdateCmd;
import com.kernotec.driverschedule.service.common.dto.Coordinate;
import com.kernotec.driverschedule.service.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.service.resource.rest.dto.request.LocationUpdateRequest;
import com.kernotec.driverschedule.service.resource.socket.LocationSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
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

    private final LocationUpdateCmd locationUpdateCmd;
    private final LocationSocketHandler locationSocketHandler;

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

        locationSocketHandler.emitMessage(LocationSocketHandler.Request.builder()
            .locationId(request.locationId())
            .topic(WebSocketTopic.LOCATION_UPDATED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID locationId,
                          @NotNull LocationUpdateRequest locationUpdateRequest)
    {

    }
}
