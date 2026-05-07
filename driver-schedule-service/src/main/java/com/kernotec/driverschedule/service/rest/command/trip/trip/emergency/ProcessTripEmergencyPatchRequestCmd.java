package com.kernotec.driverschedule.service.rest.command.trip.trip.emergency;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.command.trip.trip.emergency.TripEmergencyGetDtoCmd;
import com.kernotec.driverschedule.service.command.trip.trip.emergency.TripEmergencyUpdateCmd;
import com.kernotec.driverschedule.service.exception.trip.TripEmergencyStateException;
import com.kernotec.driverschedule.service.jpa.dto.trip.TripEmergencyDto;
import com.kernotec.driverschedule.service.jpa.enums.trip.TripEmergencyStateEnum;
import com.kernotec.driverschedule.service.jpa.service.trip.TripEmergencyStateService;
import com.kernotec.driverschedule.service.rest.dto.trip.request.trip.emergency.TripEmergencyPatchRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTripEmergencyPatchRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripEmergencyPatchRequestCmd.Request, Void>
{

    private final TripEmergencyStateService tripEmergencyStateService;

    private final TripEmergencyUpdateCmd tripEmergencyUpdateCmd;
    private final TripEmergencyGetDtoCmd tripEmergencyGetDtoCmd;

    @Override
    protected Void run(Request request) {
        TripEmergencyPatchRequest tripEmergencyPatchRequest = request.tripEmergencyPatchRequest;

        validateStateChange(
            request.tripEmergencyId, tripEmergencyPatchRequest.getTripEmergencyState());

        UUID tripEmergencyStateId = tripEmergencyStateService.findIdByCodeThrow(
            tripEmergencyPatchRequest.getTripEmergencyState());

        tripEmergencyUpdateCmd.withRequest(TripEmergencyUpdateCmd.Request.builder()
                .tripEmergencyId(request.tripEmergencyId)
                .tripEmergencyStateId(tripEmergencyStateId)
                .build())
            .execute();

        return null;
    }

    private void validateStateChange(UUID tripEmergencyId,
        TripEmergencyStateEnum tripEmergencyStateNext)
    {
        TripEmergencyDto tripEmergencyDto = tripEmergencyGetDtoCmd.withRequest(
                TripEmergencyGetDtoCmd.Request.builder()
                    .tripEmergencyId(tripEmergencyId)
                    .build())
            .execute();

        TripEmergencyStateEnum tripEmergencyStateCurrent = TripEmergencyStateEnum.fromValue(
            tripEmergencyDto.getTripEmergencyState()
                .getCode());

        if (!tripEmergencyStateCurrent.canTransitionTo(tripEmergencyStateNext)) {
            throw new TripEmergencyStateException(
                "transition.not.allowed",
                "'" + tripEmergencyStateNext + "'", HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record Request(@NotNull UUID tripEmergencyId,
                          @NotNull TripEmergencyPatchRequest tripEmergencyPatchRequest)
    {

    }
}