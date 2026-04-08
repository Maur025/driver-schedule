package com.kernotec.driverscheduleservice.rest.command.trip.trip;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.ScheduleTransportationUpdateCmd;
import com.kernotec.driverscheduleservice.command.trip.trip.TripGetDtoCmd;
import com.kernotec.driverscheduleservice.exception.trip.TripException;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.trip.TripDtoMapper;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.ScheduleTransportationDto;
import com.kernotec.driverscheduleservice.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.dto.trip.TripDto;
import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.ScheduleTransportationStateEnum;
import com.kernotec.driverscheduleservice.jpa.enums.trip.TripStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationStateService;
import com.kernotec.driverscheduleservice.jpa.service.trip.TripService;
import com.kernotec.driverscheduleservice.rest.dto.trip.request.trip.TripFinalizeRequest;
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
        TripDto tripDto = tripGetDtoCmd.withRequest(TripGetDtoCmd.Request.builder()
                .tripId(request.tripId)
                .build())
            .execute();

        TripStateEnum tripStateCode = TripStateEnum.fromValue(tripDto.getTripState()
            .getCode());

        if (tripStateCode.equals(TripStateEnum.FINALIZED) || tripStateCode.equals(
            TripStateEnum.SYSTEM_CLOSED))
        {
            log.warn("Trip is already finalized, no need to process finalize request again");
            return null;
        }

        processTripFinalizeRequestCmd.withRequest(ProcessTripFinalizeRequestCmd.Request.builder()
                .tripId(request.tripId)
                .tripFinalizeRequest(request.tripFinalizeRequest)
                .build())
            .execute();

        verifyAndUpdateSchedule(request.tripId, tripDto);

        return null;
    }

    private void verifyAndUpdateSchedule(UUID tripId, TripDto tripDto) {

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
            return;
        }

        List<TripDto> tripDtoList = tripDtoMapper.toDto(tripPage.getContent());
        Set<TripStateEnum> tripStateEnumSet = getTripStateEnumSet(tripDtoList, tripId);

        int tripFinalizedCount = getTripFinalizedCount(tripStateEnumSet);

        if (tripFinalizedCount < tripAssignmentIds.size()) {
            log.debug("Some trips still need to be completed");
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
    public record Request(@NotNull UUID tripId, @NotNull TripFinalizeRequest tripFinalizeRequest) {

    }
}
