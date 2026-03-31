package com.kernotec.driverscheduleservice.rest.command.trip;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.exception.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.dto.TripDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.trip.TripDtoMapper;
import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.TripService;
import com.kernotec.driverscheduleservice.rest.dto.request.trip.TripFinalizeRequest;
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
public class ProcessFlowTripFinalizeRequestCmd extends
    AbstractCommand<ProcessFlowTripFinalizeRequestCmd.Request, Void>
{

    private final TripService tripService;
    private final ScheduleTransportationStateService scheduleTransportationStateService;

    private final TripDtoMapper tripDtoMapper;

    private final TripGetDtoCmd tripGetDtoCmd;
    private final ScheduleTransportationUpdateCmd scheduleTransportationUpdateCmd;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;
    private final ProcessTripFinalizeRequestCmd processTripFinalizeRequestCmd;

    @Override
    protected Void run(Request request) {
        processTripFinalizeRequestCmd.withRequest(ProcessTripFinalizeRequestCmd.Request.builder()
                .tripId(request.tripId)
                .tripFinalizeRequest(request.tripFinalizeRequest)
                .build())
            .execute();

        verifyAndUpdateSchedule(request.tripId);

        return null;
    }

    private void verifyAndUpdateSchedule(UUID tripId) {
        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(tripId)
                .build())
            .execute();

        UUID scheduleTransportationId = tripDto.getTripAssignment()
            .getScheduleTransportationId();

        Set<UUID> tripAssignmentIds = getTripAssignmentIds(scheduleTransportationId);

        Page<Trip> tripPage = tripService.findByTripAssignmentIdInAndDeleted(
            tripAssignmentIds, false);

        if (tripPage.getTotalElements() > tripAssignmentIds.size()) {
            throw new TripException("validation.invalid", "", HttpStatus.CONFLICT.value());
        }

        if (tripPage.getTotalElements() < tripAssignmentIds.size()) {
            log.info("Trip less to assignments, schedule not finalized");
            return;
        }

        List<TripDto> tripDtoList = tripDtoMapper.toDto(tripPage.getContent());
        Set<TripStateEnum> tripStateEnumSet = getTripStateEnumSet(tripDtoList);

        int tripFinalizedCount = getTripFinalizedCount(tripStateEnumSet);

        if (tripFinalizedCount < tripAssignmentIds.size()) {
            log.info("Some trips still need to be completed");
            return;
        }

        UUID scheduleStateFinalizedId = scheduleTransportationStateService.findIdByCodeThrow(
            ScheduleTransportationStateEnum.FINALIZED);

        scheduleTransportationUpdateCmd.withRequest(
                ScheduleTransportationUpdateCmd.Request.builder()
                    .scheduleTransportationId(scheduleTransportationId)
                    .scheduleTransportationStateId(scheduleStateFinalizedId)
                    .build())
            .execute();
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

    private Set<TripStateEnum> getTripStateEnumSet(List<TripDto> tripDtoList) {
        return tripDtoList.stream()
            .map(tripDto -> {
                if (tripDto.getTripState() == null) {
                    return null;
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
    public record Request(@NotNull UUID tripId, @NotNull TripFinalizeRequest tripFinalizeRequest) {

    }
}
