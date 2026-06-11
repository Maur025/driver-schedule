package com.kernotec.driverschedule.service.scheduling.common.util;

import com.kernotec.driverschedule.service.scheduling.command.schedule.TripAssignmentManyCreateCmd;
import com.kernotec.driverschedule.service.scheduling.exception.ScheduleTransportationException;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.ScheduleTransportationStateEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.enums.TripAssignmentStateCodeEnum;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TripAssignmentStateService;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.TripAssignmentCreateRequest;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.request.TripAssignmentEntityMapper;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleTransportationUtil {

    private final TripAssignmentStateService tripAssignmentStateService;

    private final TripAssignmentEntityMapper tripAssignmentEntityMapper;
    private final TripAssignmentManyCreateCmd tripAssignmentManyCreateCmd;

    public void registryTripAssignments(RegistryTripAssignmentRequest request)
    {
        validateListOfAssignments(request.tripAssignmentCreateRequestList);

        UUID tripAssignmentStateActiveId = tripAssignmentStateService.findIdByCodeThrow(
            TripAssignmentStateCodeEnum.ACTIVE);

        List<TripAssignmentCreateRequest> requestWithDatesList = request.tripAssignmentCreateRequestList.stream()
            .map(currentRequest -> {
                currentRequest.setEstimatedStartTime(request.estimatedStartTime);
                currentRequest.setEstimatedEndTime(request.estimatedEndTime);
                return currentRequest;
            })
            .toList();

        List<TripAssignment> tripAssignmentListToSave = tripAssignmentEntityMapper.toEntity(
            requestWithDatesList, request.scheduleTransportationId, tripAssignmentStateActiveId);

        tripAssignmentManyCreateCmd.withRequest(TripAssignmentManyCreateCmd.Request.builder()
                .tripAssignmentList(tripAssignmentListToSave)
                .build())
            .execute();
    }

    public void updateTripAssignments(RegistryTripAssignmentRequest request) {
        validateListOfAssignments(request.tripAssignmentCreateRequestList);
    }

    private void validateListOfAssignments(
        List<TripAssignmentCreateRequest> tripAssignmentCreateRequestList)
    {
        if (tripAssignmentCreateRequestList == null || tripAssignmentCreateRequestList.isEmpty()) {
            throw new ScheduleTransportationException(
                "assignments.not.found", "", HttpStatus.BAD_REQUEST.value());
        }
    }

    public <T> Set<T> getValuesOfTripAssignmentRequest(
        Collection<TripAssignmentCreateRequest> tripAssignmentRequestCollection,
        Function<TripAssignmentCreateRequest, T> getValueFn)
    {
        return tripAssignmentRequestCollection.stream()
            .map(getValueFn)
            .collect(Collectors.toSet());
    }

    public ScheduleTransportationStateEnum getCurrentScheduleStateOfDto(
        ScheduleTransportationDto scheduleTransportationDto)
    {
        return ScheduleTransportationStateEnum.fromValue(
            scheduleTransportationDto.getScheduleTransportationState()
                .getCode());
    }

    public void validateTransitionOfDto(ScheduleTransportationDto scheduleTransportationDto,
        ScheduleTransportationStateEnum nextState)
    {
        ScheduleTransportationStateEnum currentState = getCurrentScheduleStateOfDto(
            scheduleTransportationDto);

        if (!currentState.canTransitionTo(nextState)) {
            throw new ScheduleTransportationException(
                "invalid.state.to.action",
                "'" + currentState + "'", HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record RegistryTripAssignmentRequest(
        List<TripAssignmentCreateRequest> tripAssignmentCreateRequestList,
        UUID scheduleTransportationId, ZonedDateTime estimatedStartTime,
        ZonedDateTime estimatedEndTime)
    {

    }
}
