package com.kernotec.driverschedule.service.scheduling.rest.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import com.kernotec.driverschedule.service.scheduling.command.trip.EmergencyReasonCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripEmergencyCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripEmergencyLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripLogCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.exception.TripException;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripEmergencyStateService;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripStateService;
import com.kernotec.driverschedule.service.scheduling.notification.TripEmergencyPushNotification;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.trip.TripEmergencyRequest;
import com.kernotec.driverschedule.service.scheduling.socket.TripEmergencySocketHandler;
import com.kernotec.driverschedule.service.scheduling.socket.TripSocketTopic;
import jakarta.validation.constraints.NotNull;
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

    private final TripGetDtoCmd tripGetDtoCmd;
    private final TripUpdateCmd tripUpdateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final TripEmergencyCreateCmd tripEmergencyCreateCmd;
    private final EmergencyReasonCreateCmd emergencyReasonCreateCmd;
    private final TripEmergencyLogCreateCmd tripEmergencyLogCreateCmd;

    private final TripEmergencySocketHandler tripEmergencySocketHandler;
    private final TripEmergencyPushNotification tripPushNotification;

    @Override
    protected Void run(Request request) {
        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId())
                .build())
            .execute();

        TripStateEnum tripStateCurrent = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        if (!tripStateCurrent.canTransitionTo(TripStateEnum.EMERGENCY)) {
            throw new TripException(
                "action.not.available", TripStateEnum.EMERGENCY.toString(),
                HttpStatus.CONFLICT.value()
            );
        }

        TripEmergencyRequest tripEmergencyRequest = request.tripEmergencyRequest();

        Coordinate coordinate = locationService.getCoordinateOfList(
            Arrays.asList(tripEmergencyRequest.getLongitude(), tripEmergencyRequest.getLatitude()));

        UUID tripEmergencyId = createTripEmergency(
            request.tripId(), tripDto, tripEmergencyRequest, coordinate);

        markTripInEmergency(request.tripId(), coordinate);

        tripPushNotification.onReported(tripEmergencyId);

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(tripEmergencyId)
            .topic(TripSocketTopic.TRIP_EMERGENCY_REPORTED)
            .build());

        return null;
    }

    private UUID createTripEmergency(UUID tripId, TripDto tripDto,
        TripEmergencyRequest tripEmergencyRequest, Coordinate coordinate)
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
                    .coordinate(coordinate)
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

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripEmergencyRequest tripEmergencyRequest)
    {

    }
}
