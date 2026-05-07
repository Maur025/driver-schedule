package com.kernotec.driverschedule.service.command.trip.trip.observation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.trip.TripObservation;
import com.kernotec.driverschedule.service.jpa.service.trip.TripObservationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripObservationCreateCmd extends
    AbstractTransactionalRequiredCommand<TripObservationCreateCmd.Request, UUID>
{

    private final TripObservationService tripObservationService;

    @Override
    protected UUID run(Request request) {
        var tripObservation = new TripObservation();

        tripObservation.setTripId(request.tripId);
        tripObservation.setObservationId(request.observationId);
        tripObservation.setOtherObservation(request.otherObservation);

        tripObservation = tripObservationService.save(tripObservation);
        return tripObservation.getId();
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull UUID observationId,
                          String otherObservation)
    {

    }
}