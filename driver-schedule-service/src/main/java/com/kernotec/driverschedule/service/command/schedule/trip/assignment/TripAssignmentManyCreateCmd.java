package com.kernotec.driverschedule.service.command.schedule.trip.assignment;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.jpa.service.schedule.TripAssignmentService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripAssignmentManyCreateCmd extends
    AbstractTransactionalRequiredCommand<TripAssignmentManyCreateCmd.Request, List<TripAssignment>>
{

    private final TripAssignmentService tripAssignmentService;

    @Override
    protected List<TripAssignment> run(Request request) {
        if (request.tripAssignmentList.isEmpty()) {
            log.debug("No trip assignments to create.");
            return null;
        }

        return tripAssignmentService.saveAll(request.tripAssignmentList);
    }

    @Builder
    public record Request(@NotNull List<TripAssignment> tripAssignmentList) {

    }
}
