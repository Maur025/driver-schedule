package com.kernotec.driverschedule.service.trip.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.jpa.service.TripService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripManyUpdateCmd extends
    AbstractTransactionalRequiredCommand<TripManyUpdateCmd.Request, Void>
{

    private final TripService tripService;

    @Override
    protected Void run(Request request) {
        if (request.tripList()
            .isEmpty())
        {
            return null;
        }

        tripService.saveAll(request.tripList());

        return null;
    }

    @Builder
    public record Request(@NotNull List<Trip> tripList) {

    }
}