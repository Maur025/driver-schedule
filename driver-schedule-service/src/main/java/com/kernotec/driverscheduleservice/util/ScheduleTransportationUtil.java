package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.command.schedule.trip.assignment.TripAssignmentManyCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.request.trip.assignment.TripAssignmentCreateRequest;
import com.kernotec.driverscheduleservice.rest.mapper.request.trip.assignment.TripAssignmentEntityMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleTransportationUtil {

    private final TripAssignmentEntityMapper tripAssignmentEntityMapper;
    private final TripAssignmentManyCreateCmd tripAssignmentManyCreateCmd;

    public void registryTripAssignments(
        List<TripAssignmentCreateRequest> tripAssignmentCreateRequestList,
        UUID scheduleTransportationId)
    {
        if (tripAssignmentCreateRequestList == null || tripAssignmentCreateRequestList.isEmpty()) {
            log.debug("No trip assignments to registry.");
            return;
        }

        List<TripAssignment> tripAssignmentListToSave = tripAssignmentEntityMapper.toEntity(
            tripAssignmentCreateRequestList, scheduleTransportationId);

        tripAssignmentManyCreateCmd.withRequest(TripAssignmentManyCreateCmd.Request.builder()
                .tripAssignmentList(tripAssignmentListToSave)
                .build())
            .execute();

    }
}
