package com.kernotec.driverschedule.service.scheduling.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverschedule.service.scheduling.jpa.service.ScheduleTransportationStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationStateCreateCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationStateCreateCmd.Request, UUID>
{

    private final ScheduleTransportationStateService scheduleTransportationStateService;

    @Override
    protected UUID run(Request request) {
        var scheduleTransportationState = new ScheduleTransportationState();

        scheduleTransportationState.setName(request.name);
        scheduleTransportationState.setCode(request.code);

        scheduleTransportationState = scheduleTransportationStateService.save(
            scheduleTransportationState);
        return scheduleTransportationState.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String code) {

    }
}
