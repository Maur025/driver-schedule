package com.kernotec.driverscheduleservice.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.jpa.service.TripService;
import jakarta.validation.constraints.NotNull;
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

        tripService.save(trip);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId, UUID tripStateId) {

    }
}
