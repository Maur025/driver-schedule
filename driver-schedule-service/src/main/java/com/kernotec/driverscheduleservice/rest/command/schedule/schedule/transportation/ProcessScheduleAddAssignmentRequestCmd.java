package com.kernotec.driverscheduleservice.rest.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessScheduleAddAssignmentRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessScheduleAddAssignmentRequestCmd.Request, Void>
{

    @Override
    protected Void run(Request request) {
        return null;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId) {

    }
}