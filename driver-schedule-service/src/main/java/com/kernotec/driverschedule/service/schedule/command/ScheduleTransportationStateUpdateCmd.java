package com.kernotec.driverschedule.service.schedule.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverschedule.service.schedule.jpa.service.ScheduleTransportationStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationStateUpdateCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationStateUpdateCmd.Request, Void>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;

    @Override
    protected Void run(Request request) {
        ScheduleTransportationState scheduleTransportationState = scheduleTransportationStateService.findByIdThrow(
            request.scheduleTransportationId);

        if (request.name != null) {
            scheduleTransportationState.setName(request.name);
        }

        scheduleTransportationStateService.save(scheduleTransportationState);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId, String name) {

    }
}
