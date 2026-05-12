package com.kernotec.driverschedule.service.trip.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.trip.command.EmergencyRejectReasonCreateCmd;
import com.kernotec.driverschedule.service.trip.command.TripEmergencyGetDtoCmd;
import com.kernotec.driverschedule.service.trip.command.TripEmergencyUpdateCmd;
import com.kernotec.driverschedule.service.trip.command.TripLogCreateCmd;
import com.kernotec.driverschedule.service.trip.command.TripUpdateCmd;
import com.kernotec.driverschedule.service.trip.exception.TripEmergencyException;
import com.kernotec.driverschedule.service.trip.exception.TripException;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripEmergencyDto;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.service.TripEmergencyStateService;
import com.kernotec.driverschedule.service.trip.jpa.service.TripStateService;
import com.kernotec.driverschedule.service.trip.notification.TripEmergencyPushNotification;
import com.kernotec.driverschedule.service.trip.rest.dto.request.TripEmergencyDismissRequest;
import com.kernotec.driverschedule.service.trip.socket.TripEmergencySocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripEmergencyDismissCmd extends
    AbstractTransactionalRequiredCommand<TripEmergencyDismissCmd.Request, Void>
{

    private final TripEmergencyGetDtoCmd tripEmergencyGetDtoCmd;
    private final EmergencyRejectReasonCreateCmd emergencyRejectReasonCreateCmd;
    private final TripEmergencyStateService tripEmergencyStateService;
    private final TripStateService tripStateService;
    private final TripEmergencyUpdateCmd tripEmergencyUpdateCmd;
    private final TripUpdateCmd tripUpdateCmd;
    private final TripEmergencySocketHandler tripEmergencySocketHandler;
    private final TripLogCreateCmd tripLogCreateCmd;
    private final TripEmergencyPushNotification tripEmergencyPushNotification;

    @Override
    protected Void run(Request request) {
        TripEmergencyDismissRequest tripEmergencyDismissRequest = request.tripEmergencyDismissRequest();

        TripEmergencyDto tripEmergencyDto = tripEmergencyGetDtoCmd.withRequest(
                TripEmergencyGetDtoCmd.Request.builder()
                    .tripEmergencyId(request.tripEmergencyId())
                    .build())
            .execute();

        validateDismiss(tripEmergencyDto);

        UUID tripEmergencyDismissedStateId = tripEmergencyStateService.findIdByCodeThrow(
            TripEmergencyStateEnum.DISMISSED);

        UUID tripWaitingStateId = tripStateService.findIdByCodeThrow(TripStateEnum.WAITING);

        emergencyRejectReasonCreateCmd.withRequest(EmergencyRejectReasonCreateCmd.Request.builder()
                .otherReason(tripEmergencyDismissRequest.getOtherReason())
                .reasonId(tripEmergencyDismissRequest.getReasonId())
                .tripEmergencyId(request.tripEmergencyId())
                .build())
            .execute();

        tripEmergencyUpdateCmd.withRequest(TripEmergencyUpdateCmd.Request.builder()
                .tripEmergencyId(request.tripEmergencyId())
                .tripEmergencyStateId(tripEmergencyDismissedStateId)
                .build())
            .execute();

        tripUpdateCmd.withRequest(TripUpdateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripWaitingStateId)
                .tripEnd(ZonedDateTime.now())
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripEmergencyDto.getTripId())
                .tripStateId(tripWaitingStateId)
                .build())
            .execute();

        tripEmergencyPushNotification.onDismissed(
            request.tripEmergencyId(), Set.of(tripEmergencyDto.getPersonEmergencyReportedId()));

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(request.tripEmergencyId())
            .topic(WebSocketTopic.TRIP_EMERGENCY_DISMISSED_TO_USER)
            .toList(Set.of(tripEmergencyDto.getPersonEmergencyReported()
                .getUserId()))
            .build());

        return null;
    }

    private void validateDismiss(TripEmergencyDto tripEmergencyDto) {
        TripEmergencyStateEnum emergenncyCurrentState = TripEmergencyStateEnum.fromValue(
            tripEmergencyDto.getTripEmergencyState()
                .getCode());

        if (!emergenncyCurrentState.canTransitionTo(TripEmergencyStateEnum.DISMISSED)) {
            throw new TripEmergencyException(
                "transition.not.allowed",
                "'" + TripEmergencyStateEnum.DISMISSED + "'", HttpStatus.CONFLICT.value()
            );
        }

        TripStateEnum tripCurrentState = TripStateEnum.fromValue(tripEmergencyDto.getTrip()
            .getTripState()
            .getCode());

        if (!tripCurrentState.equals(TripStateEnum.EMERGENCY)) {
            throw new TripException(
                "action.not.available",
                "'" + TripEmergencyStateEnum.DISMISSED + "'", HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId,
                          @NotNull TripEmergencyDismissRequest tripEmergencyDismissRequest)
    {

    }
}