package com.kernotec.driverscheduleservice.rest.command.trip.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripUpdatePatchRequest;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private final TripService tripService;

    private final TripResponseMapper tripResponseMapper;

    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final TripGetDtoCmd tripGetDtoCmd;

    @Override
    protected void validate(Request request) {
        TripUpdatePatchRequest tripUpdatePatchRequest = request.tripUpdatePatchRequest;

        if (tripUpdatePatchRequest.getTripStateCode() == null) {
            return;
        }

        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId)
                .build())
            .execute();

        TripStateEnum tripStateCurrent = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        if (!tripStateCurrent.canTransitionTo(tripUpdatePatchRequest.getTripStateCode())) {
            throw new TripException(
                "action.not.available", "'" + tripUpdatePatchRequest.getTripStateCode() + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

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
            Coordinate coordinate = locationService.getCoordinateOfList(
                Arrays.asList(
                    tripUpdatePatchRequest.getLongitude(),
                    tripUpdatePatchRequest.getLatitude()
                ));

            tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                    .tripId(request.tripId)
                    .tripStateId(tripStateId)
                    .coordinate(coordinate)
                    .build())
                .execute();
        }

        emitWebSocketMessage(request.tripId);

        return null;
    }

    private void emitWebSocketMessage(UUID tripId) {
        Trip trip = tripService.findByIdThrow(tripId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRIP_CHANGED, WebSocketSingleResponse.builder()
                .timestamp(ZonedDateTime.now())
                .data(tripResponseMapper.toResponse(trip))
                .topic(WebSocketTopic.TRIP_CHANGED)
                .build()
        );
    }

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripUpdatePatchRequest tripUpdatePatchRequest)
    {

    }
}
