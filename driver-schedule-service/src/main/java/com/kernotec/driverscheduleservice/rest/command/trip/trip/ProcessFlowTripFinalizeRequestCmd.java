package com.kernotec.driverscheduleservice.rest.command.trip.trip;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFinalizeRequest;
import com.kernotec.driverscheduleservice.rest.socket.trip.TripSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
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
            .topic(WebSocketTopic.TRIP_FINALIZED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID tripId, @NotNull TripFinalizeRequest tripFinalizeRequest) {

    }
}
