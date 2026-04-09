package com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.emergency.reason.EmergencyReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.TripEmergencyCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.log.TripEmergencyLogCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripEmergency;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyRequest;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.emergency.TripEmergencyResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.emergency.TripEmergencyResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTripEmergencyRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripEmergencyRequestCmd.Request, Void>
{

    private final TripStateService tripStateService;
    private final LocationService locationService;
    private final TripEmergencyStateService tripEmergencyStateService;
    private final PersonService personService;
    private final TripEmergencyService tripEmergencyService;

    private final TripEmergencyResponseMapper tripEmergencyResponseMapper;

    private final TripGetDtoCmd tripGetDtoCmd;
    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final TripEmergencyCreateCmd tripEmergencyCreateCmd;
    private final EmergencyReasonCreateCmd emergencyReasonCreateCmd;
    private final TripEmergencyLogCreateCmd tripEmergencyLogCreateCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected Void run(Request request) {
        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId)
                .build())
            .execute();

        TripStateEnum tripStateCode = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        if (tripStateCode.equals(TripStateEnum.EMERGENCY)) {
            log.warn(
                "Trip is already in emergency state, no need to process emergency request again");
            return null;
        }

        if (tripStateCode.equals(TripStateEnum.FINALIZED) || tripStateCode.equals(
            TripStateEnum.SYSTEM_CLOSED))
        {
            throw new TripException(
                "action.not.available", TripStateEnum.EMERGENCY.toString(),
                HttpStatus.CONFLICT.value()
            );
        }

        TripEmergencyRequest tripEmergencyRequest = request.tripEmergencyRequest;

        Coordinate coordinate = locationService.getCoordinateOfList(
            Arrays.asList(tripEmergencyRequest.getLongitude(), tripEmergencyRequest.getLatitude()));

        UUID tripEmergencyId = createTripEmergency(request.tripId, tripDto, tripEmergencyRequest);

        markTripInEmergency(request.tripId, coordinate);

        emitWebSocketMessage(tripEmergencyId);

        return null;
    }

    private UUID createTripEmergency(UUID tripId, TripDto tripDto,
        TripEmergencyRequest tripEmergencyRequest)
    {
        UUID tripEmergencyStateReportedId = tripEmergencyStateService.findIdByCodeThrow(
            TripEmergencyStateEnum.REPORTED);

        UUID scheduleTransportationId = tripDto.getTripAssignment()
            .getScheduleTransportationId();

        UUID tripEmergencyId = tripEmergencyCreateCmd.withRequest(
                TripEmergencyCreateCmd.Request.builder()
                    .tripId(tripId)
                    .personEmergencyReportedId(personService.findIdByUserIdAuthenticateThrow())
                    .scheduleTransportationId(scheduleTransportationId)
                    .tripEmergencyStateId(tripEmergencyStateReportedId)
                    .build())
            .execute();

        tripEmergencyLogCreateCmd.withRequest(TripEmergencyLogCreateCmd.Request.builder()
                .tripEmergencyStateId(tripEmergencyStateReportedId)
                .tripEmergencyId(tripEmergencyId)
                .build())
            .execute();

        emergencyReasonCreateCmd.withRequest(EmergencyReasonCreateCmd.Request.builder()
                .tripEmergencyId(tripEmergencyId)
                .reasonId(tripEmergencyRequest.getReasonId())
                .otherReason(tripEmergencyRequest.getOtherReason())
                .build())
            .execute();

        return tripEmergencyId;
    }

    private void markTripInEmergency(UUID tripId, Coordinate coordinate) {
        UUID tripStateEmergencyId = tripStateService.findIdByCodeThrow(TripStateEnum.EMERGENCY);

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(tripId)
                .tripStateId(tripStateEmergencyId)
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripId)
                .tripStateId(tripStateEmergencyId)
                .coordinate(coordinate)
                .build())
            .execute();
    }

    private void emitWebSocketMessage(UUID tripEmergencyId) {
        TripEmergency tripEmergency = tripEmergencyService.findByIdThrow(tripEmergencyId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRIP_EMERGENCY_REPORTED,
            WebSocketSingleResponse.<TripEmergencyResponse>builder()
                .timestamp(ZonedDateTime.now())
                .data(tripEmergencyResponseMapper.toResponse(tripEmergency))
                .topic(WebSocketTopic.TRIP_EMERGENCY_REPORTED)
                .build()
        );
    }

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripEmergencyRequest tripEmergencyRequest)
    {

    }
}
