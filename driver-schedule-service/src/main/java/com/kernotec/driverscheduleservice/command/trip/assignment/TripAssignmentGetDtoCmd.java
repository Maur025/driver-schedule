package com.kernotec.driverscheduleservice.command.trip.assignment;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.dto.TripAssignmentDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.TripAssignmentDtoMapper;
import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.jpa.service.TripAssignmentService;
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
