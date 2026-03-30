package com.kernotec.driverscheduleservice.rest.command.trip;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.TripCreateCmd;
import com.kernotec.driverscheduleservice.command.trip.assignment.TripAssignmentGetDtoCmd;
import com.kernotec.driverscheduleservice.command.trip.log.TripLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.TripStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.TripCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTripCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTripCreateRequestCmd.Request, UUID>
{

    private final TripStateService tripStateService;
    private final ScheduleTransportationStateService scheduleTransportationStateService;

    private final TripAssignmentGetDtoCmd tripAssignmentGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final TripCreateCmd tripCreateCmd;
    private final TripLogCreateCmd tripLogCreateCmd;

    @Override
    protected UUID run(Request request) {
        TripCreateRequest tripCreateRequest = request.tripCreateRequest;

        TripAssignmentDto tripAssignmentDto = tripAssignmentGetDtoCmd.withRequest(
                TripAssignmentGetDtoCmd.Request.builder()
                    .tripAssignmentId(tripCreateRequest.getTripAssignmentId())
                    .build())
            .execute();

        ScheduleTransportationStateEnum scheduleTransportationStateCode = ScheduleTransportationStateEnum.fromValue(
            tripAssignmentDto.getScheduleTransportation()
                .getScheduleTransportationState()
                .getCode());

        if (ScheduleTransportationStateEnum.CANCELLED.equals(scheduleTransportationStateCode)
            || ScheduleTransportationStateEnum.FINALIZED.equals(scheduleTransportationStateCode))
        {
            throw new ScheduleTransportationException(
                "not.supported.state",
                "'" + scheduleTransportationStateCode + "'", HttpStatus.CONFLICT.value()
            );
        }

        updateScheduleStateToInProgress(
            scheduleTransportationStateCode, tripAssignmentDto.getScheduleTransportationId());

        UUID tripStateOnRouteId = tripStateService.findIdByCodeThrow(TripStateEnum.ON_ROUTE);

        UUID tripId = tripCreateCmd.withRequest(TripCreateCmd.Request.builder()
                .tripStateId(tripStateOnRouteId)
                .tripAssignmentId(tripCreateRequest.getTripAssignmentId())
                .build())
            .execute();

        tripLogCreateCmd.withRequest(TripLogCreateCmd.Request.builder()
                .tripId(tripId)
                .tripStateId(tripStateOnRouteId)
                .build())
            .execute();

        return tripId;
    }

    private void updateScheduleStateToInProgress(
        ScheduleTransportationStateEnum scheduleTransportationState, UUID scheduleTransportationId)
    {
        if (ScheduleTransportationStateEnum.IN_PROGRESS.equals(scheduleTransportationState)) {
            return;
        }

        UUID scheduleStateInProgressId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.IN_PROGRESS);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleStateInProgressId)
                    .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull TripCreateRequest tripCreateRequest) {

    }
}
