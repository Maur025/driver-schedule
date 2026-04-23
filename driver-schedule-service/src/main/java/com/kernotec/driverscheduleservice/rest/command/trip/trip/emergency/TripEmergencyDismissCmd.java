package com.kernotec.driverscheduleservice.rest.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.trip.emergency.reject.reason.EmergencyRejectReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.TripEmergencyGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.emergency.TripEmergencyUpdateCmd;
import com.kernotec.driverscheduleservice.exception.trip.TripEmergencyException;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripEmergencyStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.emergency.TripEmergencyDismissRequest;
import com.kernotec.driverscheduleservice.rest.socket.trip.TripEmergencySocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
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
                .build())
            .execute();

        tripEmergencySocketHandler.emitMessage(TripEmergencySocketHandler.Request.builder()
            .tripEmergencyId(request.tripEmergencyId())
            .topic(WebSocketTopic.TRIP_EMERGENCY_DISMISSED_TO_USER)
            .build());

        return null;
    }

    private void validateDismiss(TripEmergencyDto tripEmergencyDto) {
        TripEmergencyStateEnum emergenncyCurrentState = TripEmergencyStateEnum.fromValue(
            tripEmergencyDto.getTripEmergencyState()
                .getCode());

        if (!emergenncyCurrentState.canTransitionTo(TripEmergencyStateEnum.DISMISSED)) {
            throw new TripEmergencyException(
                "transition.not.allowed", "'" + TripEmergencyStateEnum.DISMISSED + "'",
                HttpStatus.CONFLICT.value()
            );
        }

        TripStateEnum tripCurrentState = TripStateEnum.fromValue(tripEmergencyDto.getTrip()
            .getTripState()
            .getCode());

        if (!tripCurrentState.equals(TripStateEnum.EMERGENCY)) {
            throw new TripException(
                "action.not.available", "'" + TripEmergencyStateEnum.DISMISSED + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId,
                          @NotNull TripEmergencyDismissRequest tripEmergencyDismissRequest)
    {

    }
}