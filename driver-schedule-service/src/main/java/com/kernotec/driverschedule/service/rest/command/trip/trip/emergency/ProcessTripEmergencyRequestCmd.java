package com.kernotec.driverschedule.service.rest.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.command.trip.emergency.reason.EmergencyReasonCreateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverschedule.service.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.emergency.TripEmergencyCreateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.emergency.log.TripEmergencyLogCreateCmd;
import com.kernotec.driverschedule.service.command.trip.trip.log.TripLogCreateCmd;
import com.kernotec.driverschedule.service.exception.trip.TripException;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripDto;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverschedule.service.jpa.service.resource.LocationService;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.jpa.service.trip.TripEmergencyStateService;
import com.kernotec.driverschedule.service.jpa.service.trip.TripStateService;
import com.kernotec.driverschedule.service.common.dto.Coordinate;
import com.kernotec.driverschedule.service.notification.dto.NotificationSendRequest;
import com.kernotec.driverschedule.service.notification.service.NotificationOrchestrator;
import com.kernotec.driverschedule.service.notification.templates.NotificationTemplate.TripEmergencyReportedTemplate;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.emergency.TripEmergencyRequest;
import com.kernotec.driverschedule.service.rest.socket.trip.TripEmergencySocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Map;
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
    private final NotificationOrchestrator notificationOrchestrator;

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

        UUID tripEmergencyId = createTripEmergency(request.tripId(), tripDto, tripEmergencyRequest);

        markTripInEmergency(request.tripId(), coordinate);

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(TripEmergencyReportedTemplate.TITLE)
            .body(TripEmergencyReportedTemplate.BODY)
            .campaignRecipient(TripEmergencyReportedTemplate.RECEIVER)
            .dataMap(Map.of("screen", "trip-emergency/" + tripEmergencyId))
            .build());

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(tripEmergencyId)
            .topic(WebSocketTopic.TRIP_EMERGENCY_REPORTED)
            .build());

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

    @Builder
    public record Request(@NotNull UUID tripId,
                          @NotNull TripEmergencyRequest tripEmergencyRequest)
    {

    }
}
