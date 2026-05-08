package com.kernotec.driverschedule.service.rest.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverschedule.service.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverschedule.service.common.dto.Coordinate;
import com.kernotec.driverschedule.service.exception.trip.TripException;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripDto;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.service.resource.LocationService;
import com.kernotec.driverschedule.service.jpa.service.trip.TripStateService;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.TripUpdatePatchRequest;
import com.kernotec.driverschedule.service.rest.socket.trip.TripSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTripPatchUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripPatchUpdateRequestCmd.Request, Void>
{

    private final TripStateService tripStateService;
    private final LocationService locationService;

    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final TripGetDtoCmd tripGetDtoCmd;

    private final TripSocketHandler tripSocketHandler;

    @Override
    protected void validate(Request request) {
        TripUpdatePatchRequest tripUpdatePatchRequest = request.tripUpdatePatchRequest();

        if (tripUpdatePatchRequest.getTripStateCode() == null) {
            return;
        }

        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId())
                .build())
            .execute();

        TripStateEnum tripStateCurrent = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        TripStateEnum nextTripState = tripUpdatePatchRequest.getTripStateCode();

        if (nextTripState.equals(TripStateEnum.FINALIZED) || nextTripState.equals(
            TripStateEnum.EMERGENCY) || nextTripState.equals(TripStateEnum.SYSTEM_CLOSED))
        {
            throw new TripException(
                "action.no.available", "'" + nextTripState + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }

        if (!tripStateCurrent.canTransitionTo(nextTripState)) {
            throw new TripException(
                "action.not.available", "'" + nextTripState + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Override
    protected Void run(Request request) {
        TripUpdatePatchRequest tripUpdatePatchRequest = request.tripUpdatePatchRequest();

        UUID tripStateId = tripStateService.findIdByCodeThrow(
            tripUpdatePatchRequest.getTripStateCode());

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(request.tripId())
                .tripStateId(tripStateId)
                .build())
            .execute();

        if (tripUpdatePatchRequest.getTripStateCode() != null) {
            Coordinate coordinate = locationService.getCoordinateOfList(
                Arrays.asList(
                    tripUpdatePatchRequest.getLongitude(),
                    tripUpdatePatchRequest.getLatitude()
                ));

            tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                    .tripId(request.tripId())
                    .tripStateId(tripStateId)
                    .coordinate(coordinate)
                    .build())
                .execute();
        }

        tripSocketHandler.emitMessage(TripSocketHandler.Request.builder()
            .tripId(request.tripId())
            .topic(WebSocketTopic.TRIP_CHANGED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripUpdatePatchRequest tripUpdatePatchRequest)
    {

    }
}
