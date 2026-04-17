package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.command.schedule.trip.assignment.TripAssignmentManyCreateCmd;
import com.kernotec.driverscheduleservice.exception.schedule.ScheduleTransportationException;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.enums.schedule.TripAssignmentStateCodeEnum;
import com.kernotec.driverscheduleservice.jpa.service.schedule.TripAssignmentStateService;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.request.trip.assignment.TripAssignmentEntityMapper;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
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

    @Builder
    public record RegistryTripAssignmentRequest(
        List<TripAssignmentCreateRequest> tripAssignmentCreateRequestList,
        UUID scheduleTransportationId, ZonedDateTime estimatedStartTime,
        ZonedDateTime estimatedEndTime)
    {

    }
}
