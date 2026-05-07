package com.kernotec.driverscheduleservice.rest.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.common.dto.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFinalizeRequest;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTripFinalizeRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripFinalizeRequestCmd.Request, Void>
{

    private final TripStateService tripStateService;
    private final LocationService locationService;

    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;

    @Override
    protected Void run(Request request) {
        TripFinalizeRequest tripFinalizeRequest = request.tripFinalizeRequest;

        UUID tripStateFinalizedId = tripStateService.findIdByCodeThrow(TripStateEnum.FINALIZED);

        Coordinate coordinate = locationService.getCoordinateOfList(
            Arrays.asList(tripFinalizeRequest.getLongitude(), tripFinalizeRequest.getLatitude()));

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(request.tripId)
                .tripEnd(ZonedDateTime.now())
                .durationTotalMinutes(tripFinalizeRequest.getDurationTotalMinutes())
                .onRouteTimeMinutes(tripFinalizeRequest.getOnRouteTimeMinutes())
                .waitTimeMinutes(tripFinalizeRequest.getWaitTimeMinutes())
                .description(tripFinalizeRequest.getDescription())
                .tripStateId(tripStateFinalizedId)
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(request.tripId)
                .tripStateId(tripStateFinalizedId)
                .coordinate(coordinate)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull TripFinalizeRequest tripFinalizeRequest) {

    }
}
