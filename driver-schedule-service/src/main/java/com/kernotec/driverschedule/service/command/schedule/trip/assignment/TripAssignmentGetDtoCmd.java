package com.kernotec.driverschedule.service.command.schedule.trip.assignment;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.dto.schedule.TripAssignmentDto;
import com.kernotec.driverschedule.service.jpa.dto.mapper.schedule.trip.assignment.TripAssignmentDtoMapper;
import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.jpa.service.schedule.TripAssignmentService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TripAssignmentGetDtoCmd extends
    AbstractTransactionalRequiredCommand<TripAssignmentGetDtoCmd.Request, TripAssignmentDto>
{

    private final TripAssignmentService tripAssignmentService;
    private final TripAssignmentDtoMapper tripAssignmentDtoMapper;

    @Override
    protected TripAssignmentDto run(Request request) {
        TripAssignment tripAssignment = tripAssignmentService.findByIdThrow(
            request.tripAssignmentId);
        return tripAssignmentDtoMapper.toDto(tripAssignment);
    }

    @Builder
    public record Request(@NotNull UUID tripAssignmentId) {

    }
}
