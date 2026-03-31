package com.kernotec.driverscheduleservice.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.jpa.service.TripService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripCreateCmd extends
    AbstractTransactionalRequiredCommand<TripCreateCmd.Request, UUID>
{

    private final TripService tripService;

    @Override
    protected UUID run(Request request) {
        var trip = new Trip();

        trip.setTripStart(request.tripStart);
        trip.setTripEnd(request.tripEnd);
        trip.setDurationTotalMinutes(request.durationTotalMinutes);
        trip.setOnRouteTimeMinutes(request.onRouteTimeMinutes);
        trip.setWaitTimeMinutes(request.waitTimeMinutes);
        trip.setDescription(request.description);
        trip.setTripAssignmentId(request.tripAssignmentId);
        trip.setTripStateId(request.tripStateId);

        trip = tripService.save(trip);
        return trip.getId();
    }

    @Builder
    public record Request(@NotNull UUID tripAssignmentId, @NotNull UUID tripStateId,
                          ZonedDateTime tripStart, ZonedDateTime tripEnd,
                          Double durationTotalMinutes, Double onRouteTimeMinutes,
                          Double waitTimeMinutes, String description)
    {

    }
}
