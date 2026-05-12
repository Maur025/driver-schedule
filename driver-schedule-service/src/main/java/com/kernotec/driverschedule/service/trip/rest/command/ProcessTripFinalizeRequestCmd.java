package com.kernotec.driverschedule.service.trip.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.trip.command.TripUpdateCmd;
import com.kernotec.driverschedule.service.trip.command.TripLogCreateCmd;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.service.trip.jpa.service.TripStateService;
import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.service.trip.rest.dto.request.TripFinalizeRequest;
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
