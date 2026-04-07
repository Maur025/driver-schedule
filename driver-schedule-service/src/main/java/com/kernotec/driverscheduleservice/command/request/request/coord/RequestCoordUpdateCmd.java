package com.kernotec.driverscheduleservice.command.request.request.coord;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.request.RequestCoord;
import com.kernotec.driverscheduleservice.jpa.service.request.RequestCoordService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RequestCoordUpdateCmd extends
    AbstractTransactionalRequiredCommand<RequestCoordUpdateCmd.Request, Void>
{

    private final RequestCoordService requestCoordService;

    @Override
    protected Void run(Request request) {
        RequestCoord requestCoord = requestCoordService.findByIdThrow(request.requestCoordId);

        if (request.coordinate != null) {
            requestCoord.setCoordinate(request.coordinate);
        }
        if (request.index != null) {
            requestCoord.setIndex(request.index);
        }
        if (request.description != null) {
            requestCoord.setDescription(request.description);
        }
        if (request.locationName != null) {
            requestCoord.setLocationName(request.locationName);
        }
        if (request.durationMinutes != null) {
            requestCoord.setDurationMinutes(request.durationMinutes);
        }
        if (request.distanceKilometers != null) {
            requestCoord.setDistanceKilometers(request.distanceKilometers);
        }
        if (request.waitTimeMinutes != null) {
            requestCoord.setWaitTimeMinutes(request.waitTimeMinutes);
        }
        if (request.estimatedArrivalTime != null) {
            requestCoord.setEstimatedArrivalTime(request.estimatedArrivalTime);
        }
        if (request.locationDescription != null) {
            requestCoord.setLocationDescription(request.locationDescription);
        }
        if (request.locationId != null) {
            requestCoord.setLocationId(request.locationId);
        }

        requestCoordService.save(requestCoord);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID requestCoordId, Coordinate coordinate, Integer index,
                          String description, String locationName, Double durationMinutes,
                          Double distanceKilometers, Double waitTimeMinutes,
                          ZonedDateTime estimatedArrivalTime, String locationDescription,
                          UUID locationId)
    {

    }
}
