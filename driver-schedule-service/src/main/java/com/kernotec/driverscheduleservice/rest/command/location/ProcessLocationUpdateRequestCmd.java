package com.kernotec.driverscheduleservice.rest.command.location;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.location.LocationUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.rest.dto.request.location.LocationUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.LocationResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.location.LocationResponseMapper;
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

    private final LocationUpdateCmd locationUpdateCmd;
    private final LocationService locationService;
    private final WebSocketHandler webSocketHandler;
    private final LocationResponseMapper locationResponseMapper;

    @Override
    protected void validate(Request request) {
        LocationUpdateRequest locationUpdateRequest = request.locationUpdateRequest;

        if (!locationUpdateRequest.getCoordinates().isEmpty()){
        }
    }

    @Override
    protected Void run(Request request) {
        LocationUpdateRequest locationUpdateRequest = request.locationUpdateRequest;

        locationUpdateCmd.withRequest(LocationUpdateCmd.Request.builder()
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
