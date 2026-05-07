package com.kernotec.driverschedule.service.command.schedule.schedule.transportation;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverschedule.service.jpa.service.schedule.ScheduleTransportationService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScheduleTransportationCreateCmd extends
    AbstractTransactionalRequiredCommand<ScheduleTransportationCreateCmd.Request, UUID>
{

    private final ScheduleTransportationService scheduleTransportationService;

    @Override
    protected UUID run(Request request) {
        var scheduleTransportation = new ScheduleTransportation();

        scheduleTransportation.setScheduleFrom(request.scheduleFrom);
        scheduleTransportation.setScheduleTo(request.scheduleTo);
        scheduleTransportation.setScheduledDate(request.scheduledDate);
        scheduleTransportation.setTransportationRequestId(request.transportationRequestId);
        scheduleTransportation.setPersonRequestedId(request.personRequestedId);
        scheduleTransportation.setScheduleTransportationStateId(
            request.scheduleTransportationStateId);

        scheduleTransportation = scheduleTransportationService.save(scheduleTransportation);
        return scheduleTransportation.getId();
    }

    @Builder
    public record Request(@NotNull ZonedDateTime scheduleFrom, @NotNull ZonedDateTime scheduleTo,
                          @NotNull ZonedDateTime scheduledDate,
                          @NotNull UUID transportationRequestId, @NotNull UUID personRequestedId,
                          @NotNull UUID scheduleTransportationStateId)
    {

    }
}
