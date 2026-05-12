package com.kernotec.driverschedule.service.trip.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.schedule.command.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.schedule.command.ScheduleTransportationUpdateCmd;
import com.kernotec.driverschedule.service.schedule.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.schedule.jpa.dto.TripAssignmentDto;
import com.kernotec.driverschedule.service.schedule.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.schedule.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverschedule.service.schedule.notification.SchedulePushNotification;
import com.kernotec.driverschedule.service.schedule.socket.ScheduleTransportationSocketHandler;
import com.kernotec.driverschedule.service.trip.exception.TripException;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripDto;
import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.jpa.enums.TripStateEnum;
import com.kernotec.driverschedule.service.trip.jpa.mapper.TripDtoMapper;
import com.kernotec.driverschedule.service.trip.jpa.service.TripService;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripVerifyAndUpdateScheduleCmd extends
    AbstractTransactionalRequiredCommand<TripVerifyAndUpdateScheduleCmd.Request, Void>
{

    private final TripService tripService;
    private final ScheduleTransportationStateService scheduleTransportationStateService;

    private final TripDtoMapper tripDtoMapper;

    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;

    private final ScheduleTransportationSocketHandler scheduleTransportationSocketHandler;
    private final SchedulePushNotification schedulePushNotification;

    @Override
    protected Void run(Request request) {
        TripDto tripDto = request.tripDto();

        UUID scheduleTransportationId = tripDto.getTripAssignment()
            .getScheduleTransportationId();

        Set<UUID> tripAssignmentIds = getTripAssignmentIds(scheduleTransportationId);

        Page<Trip> tripPage = tripService.findByTripAssignmentIdInAndDeleted(
            tripAssignmentIds, false);

        if (tripPage.getTotalElements() > tripAssignmentIds.size()) {
            throw new TripException("validation.invalid", "", HttpStatus.CONFLICT.value());
        }

        if (tripPage.getTotalElements() < tripAssignmentIds.size()) {
            log.debug("Trip less to assignments, response not finalized");
            return null;
        }

        List<TripDto> tripDtoList = tripDtoMapper.toDto(tripPage.getContent());
        Set<TripStateEnum> tripStateEnumSet = getTripStateEnumSet(tripDtoList, tripDto.getId());

        int tripFinalizedCount = getTripFinalizedCount(tripStateEnumSet);

        if (tripFinalizedCount < tripAssignmentIds.size()) {
            log.debug("Some trips still need to be completed");
            return null;
        }

        UUID scheduleStateFinalizedId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.FINALIZED);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleStateFinalizedId)
                    .build())
            .execute();

        schedulePushNotification.onFinalized(scheduleTransportationId);

        scheduleTransportationSocketHandler.emitMessage(
            ScheduleTransportationSocketHandler.Request.builder()
                .scheduleTransportationId(scheduleTransportationId)
                .topic(WebSocketTopic.SCHEDULE_TRANSPORTATION_FINALIZED)
                .build());

        return null;
    }

    private Set<UUID> getTripAssignmentIds(UUID scheduleTransportationId) {
        ScheduleTransportationDto scheduleTransportationDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .build())
            .execute();

        return scheduleTransportationDto.getTripAssignments()
            .stream()
            .map(TripAssignmentDto::getId)
            .collect(Collectors.toSet());
    }

    private Set<TripStateEnum> getTripStateEnumSet(List<TripDto> tripDtoList, UUID tripId) {
        return tripDtoList.stream()
            .map(tripDto -> {
                if (tripDto.getTripState() == null) {
                    return null;
                }

                if (tripDto.getId()
                    .equals(tripId))
                {
                    log.debug("In current processing, successfull if is here, finalized");
                    return TripStateEnum.FINALIZED;
                }

                String code = tripDto.getTripState()
                    .getCode();

                return TripStateEnum.fromValue(code);
            })
            .collect(Collectors.toSet());
    }

    private int getTripFinalizedCount(Set<TripStateEnum> tripStateEnumSet) {
        int tripFinalizedCount = 0;

        for (TripStateEnum tripState : tripStateEnumSet) {
            if (tripState.equals(TripStateEnum.FINALIZED) || tripState.equals(
                TripStateEnum.SYSTEM_CLOSED))
            {
                tripFinalizedCount++;
            }
        }

        return tripFinalizedCount;
    }

    @Builder
    public record Request(@NotNull TripDto tripDto) {

    }
}