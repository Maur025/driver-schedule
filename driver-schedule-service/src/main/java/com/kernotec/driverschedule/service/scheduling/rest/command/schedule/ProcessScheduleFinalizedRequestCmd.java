package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripLogManyCreateCmd;
import com.kernotec.driverschedule.service.scheduling.command.trip.TripManyUpdateCmd;
import com.kernotec.driverschedule.service.scheduling.exception.ScheduleTransportationException;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripLog;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.mapper.TripDtoMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripService;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripStateService;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleSocketTopic;
import com.kernotec.driverschedule.service.scheduling.socket.ScheduleTransportationSocketHandler;
import com.kernotec.driverschedule.service.scheduling.socket.TripSocketHandler;
import com.kernotec.driverschedule.service.scheduling.socket.TripSocketTopic;
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
    private final TripSocketHandler tripSocketHandler;
    private final TripDtoMapper tripDtoMapper;

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
                .topic(ScheduleSocketTopic.SCHEDULE_TRANSPORTATION_FINALIZED)
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
        List<TripDto> tripDtoList = tripDtoMapper.toDto(trips);

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

        for (TripDto tripDto : tripDtoList) {
            UUID userId = tripDto.getTripAssignment()
                .getDriver()
                .getUserId();

            tripSocketHandler.emitMessage(TripSocketHandler.Request.builder()
                .tripId(tripDto.getId())
                .topic(TripSocketTopic.TRIP_FINALIZED_TO_USER)
                .toList(Set.of(userId))
                .build());
        }
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