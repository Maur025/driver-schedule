package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripManyUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.log.TripLogManyCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.entity.trip.TripLog;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripStateService;
import com.kernotec.driverscheduleservice.rest.socket.schedule.ScheduleTransportationSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessScheduleFinalizedRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleFinalizedRequestCmd.Request, Void>
{

    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final TripStateService tripStateService;
    private final TripService tripService;
    private final TripLogManyCreateCmd tripLogManyCreateCmd;
    private final TripManyUpdateCmd tripManyUpdateCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final ScheduleTransportationStateService scheduleTransportationStateService;
    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;

    @Override
    protected Void run(Request request) {
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId())
                    .build())
            .execute();

        ScheduleTransportationStateEnum scheduleCurrentState = ScheduleTransportationStateEnum.fromValue(
            scheduleTransportationDto.getScheduleTransportationState()
                .getCode());

        if (!scheduleCurrentState.canTransitionTo(ScheduleTransportationStateEnum.FINALIZED)) {
            throw new ScheduleTransportationException(
                "not.supported.state", "'" + ScheduleTransportationStateEnum.FINALIZED + "'",
                HttpStatus.CONFLICT.value()
            );
        }

        UUID scheduleStateFinalizedId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.FINALIZED);

        completeAllTrips(scheduleTransportationDto);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(request.scheduleTransportationId())
                    .scheduleTransportationStateId(scheduleStateFinalizedId)
                    .build())
            .execute();

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_FINALIZED)
                .scheduleTransportationId(request.scheduleTransportationId())
                .build());

        return null;
    }

    private void completeAllTrips(ScheduleTransportationDto scheduleTransportationDto) {
        UUID tripStateFinalizedId = tripStateService.findIdByCodeThrow(TripStateEnum.FINALIZED);
        UUID tripStateSystemClosedId = tripStateService.findIdByCodeThrow(
            TripStateEnum.SYSTEM_CLOSED);

        Set<UUID> tripIds = scheduleTransportationDto.getTripAssignments()
            .stream()
            .flatMap(tripAssignmentDto -> tripAssignmentDto.getTrips()
                .stream())
            .filter(tripDto -> !tripDto.getTripStateId()
                .equals(tripStateFinalizedId))
            .map(TripDto::getId)
            .collect(Collectors.toSet());

        List<Trip> trips = tripService.findByIdIn(tripIds);

        List<TripLog> tripLogs = new ArrayList<>();

        for (Trip trip : trips) {
            trip.setTripStateId(tripStateSystemClosedId);
            trip.setTripEnd(ZonedDateTime.now());

            tripLogs.add(getTripLog(trip.getId(), tripStateSystemClosedId));
        }

        tripManyUpdateCmd.withRequest(TripManyUpdateCmd.Request.builder()
                .tripList(trips)
                .build())
            .execute();

        tripLogManyCreateCmd.withRequest(TripLogManyCreateCmd.Request.builder()
                .tripLogList(tripLogs)
                .build())
            .execute();
    }

    private TripLog getTripLog(UUID tripId, UUID tripStateId) {
        var tripLog = new TripLog();

        tripLog.setTripId(tripId);
        tripLog.setTripStateId(tripStateId);

        return tripLog;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId) {

    }
}