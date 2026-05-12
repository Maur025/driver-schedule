package com.kernotec.driverschedule.service.trip.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.jpa.service.TripService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripUpdateCmd extends
    AbstractTransactionalRequiredCommand<TripUpdateCmd.Request, Void>
{

    private final TripService tripService;

    @Override
    protected Void run(Request request) {
        Trip trip = tripService.findByIdThrow(request.tripId);

        if (request.tripStateId != null) {
            trip.setTripStateId(request.tripStateId);
        }
        if (request.tripStart != null) {
            trip.setTripStart(request.tripStart);
        }
        if (request.tripEnd != null) {
            trip.setTripEnd(request.tripEnd);
        }
        if (request.durationTotalMinutes != null) {
            trip.setDurationTotalMinutes(request.durationTotalMinutes);
        }
        if (request.onRouteTimeMinutes != null) {
            trip.setOnRouteTimeMinutes(request.onRouteTimeMinutes);
        }
        if (request.waitTimeMinutes != null) {
            trip.setWaitTimeMinutes(request.waitTimeMinutes);
        }
        if (request.description != null) {
            trip.setDescription(request.description);
        }

        tripService.save(trip);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId, UUID tripStateId, ZonedDateTime tripStart,
                          ZonedDateTime tripEnd, Double durationTotalMinutes,
                          Double onRouteTimeMinutes, Double waitTimeMinutes, String description)
    {

    }
}
