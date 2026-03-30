package com.kernotec.driverscheduleservice.rest.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.jpa.service.TripStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.TripUpdatePatchRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTripPatchUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripPatchUpdateRequestCmd.Request, Void>
{

    private final TripStateService tripStateService;

    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;

    @Override
    protected Void run(Request request) {
        TripUpdatePatchRequest tripUpdatePatchRequest = request.tripUpdatePatchRequest;

        UUID tripStateId = tripStateService.findIdByCodeThrow(
            tripUpdatePatchRequest.getTripStateCode());

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(request.tripId)
                .tripStateId(tripStateId)
                .build())
            .execute();

        if (tripUpdatePatchRequest.getTripStateCode() != null) {
            tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                    .tripId(request.tripId)
                    .tripStateId(tripStateId)
                    .build())
                .execute();
        }

        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripUpdatePatchRequest tripUpdatePatchRequest)
    {

    }
}
