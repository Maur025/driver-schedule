package com.kernotec.driverschedule.service.scheduling.rest.command.trip;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.trip.TripFinalizeRequest;
import com.kernotec.driverschedule.service.scheduling.socket.TripSocketHandler;
import com.kernotec.driverschedule.service.scheduling.socket.TripSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessFlowTripFinalizeRequestCmd extends
    AbstractCommand<ProcessFlowTripFinalizeRequestCmd.Request, Void>
{

    private final TripGetDtoCmd tripGetDtoCmd;
    private final ProcessTripFinalizeRequestCmd processTripFinalizeRequestCmd;

    private final TripSocketHandler tripSocketHandler;
    private final TripVerifyAndUpdateScheduleCmd tripVerifyAndUpdateScheduleCmd;

    @Override
    protected Void run(Request request) {
        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId())
                .build())
            .execute();

        TripStateEnum tripStateCurrent = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        if (!tripStateCurrent.canTransitionTo(TripStateEnum.FINALIZED)) {
            log.warn("Trip is already finalized, no need to process finalize request again");
            return null;
        }

        processTripFinalizeRequestCmd.withRequest(ProcessTripFinalizeRequestCmd.Request.builder()
                .tripId(request.tripId())
                .tripFinalizeRequest(request.tripFinalizeRequest())
                .build())
            .execute();

        tripVerifyAndUpdateScheduleCmd.withRequest(TripVerifyAndUpdateScheduleCmd.Request.builder()
                .tripDto(tripDto)
                .build())
            .execute();

        tripSocketHandler.emitMessage(TripSocketHandler.Request.builder()
            .tripId(request.tripId())
            .topic(TripSocketTopic.TRIP_FINALIZED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull TripFinalizeRequest tripFinalizeRequest) {

    }
}
