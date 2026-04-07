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
public class RequestCoordCreateCmd extends
    AbstractTransactionalRequiredCommand<RequestCoordCreateCmd.Request, UUID>
{

    private final RequestCoordService requestCoordService;

    @Override
    protected UUID run(Request request) {
        var requestCoord = new RequestCoord();

        requestCoord.setCoordinate(request.coordinate);
        requestCoord.setIndex(request.index);
        requestCoord.setDescription(request.description);
        requestCoord.setLocationName(request.locationName);
        requestCoord.setDurationMinutes(request.durationMinutes);
        requestCoord.setDistanceKilometers(request.distanceKilometers);
        requestCoord.setWaitTimeMinutes(request.waitTimeMinutes);
        requestCoord.setEstimatedArrivalTime(request.estimatedArrivalTime);
        requestCoord.setLocationDescription(request.locationDescription);
        requestCoord.setTransportationRequestId(request.transportationRequestId);
        requestCoord.setLocationId(request.locationId);

        requestCoord = requestCoordService.save(requestCoord);
        return requestCoord.getId();
    }

    @Builder
    public record Request(@NotNull Coordinate coordinate, @NotNull Integer index,
                          String description, String locationName, Double durationMinutes,
                          Double distanceKilometers, Double waitTimeMinutes,
                          ZonedDateTime estimatedArrivalTime, String locationDescription,
                          @NotNull UUID transportationRequestId, UUID locationId)
    {

    }
}
