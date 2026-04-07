package com.kernotec.driverscheduleservice.command.schedule.schedule.transportation.log;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportationLog;
import com.kernotec.driverscheduleservice.jpa.service.schedule.ScheduleTransportationLogService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationLogCreateCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationLogCreateCmd.Request, UUID>
{

    private final ScheduleTransportationLogService scheduleTransportationLogService;

    @Override
    protected UUID run(Request request) {
        var scheduleTransportationLog = new ScheduleTransportationLog();

        scheduleTransportationLog.setScheduleTransportationId(request.scheduleTransportationId);
        scheduleTransportationLog.setScheduleTransportationStateId(
            request.scheduleTransportationStateId);

        scheduleTransportationLog = scheduleTransportationLogService.save(
            scheduleTransportationLog);
        return scheduleTransportationLog.getId();
    }

    @Builder
    public record Request(@NotNull UUID scheduleTransportationId,
                          @NotNull UUID scheduleTransportationStateId)
    {

    }
}
