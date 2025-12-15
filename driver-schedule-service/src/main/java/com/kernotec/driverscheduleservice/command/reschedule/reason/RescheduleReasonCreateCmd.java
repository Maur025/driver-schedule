package com.kernotec.driverscheduleservice.command.reschedule.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.RescheduleReason;
import com.kernotec.driverscheduleservice.jpa.service.RescheduleReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RescheduleReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<RescheduleReasonCreateCmd.Request, UUID>
{

    private final RescheduleReasonService rescheduleReasonService;

    @Override
    protected UUID run(Request request) {
        var rescheduleReason = new RescheduleReason();

        rescheduleReason.setReasonId(request.reasonId);
        rescheduleReason.setScheduleTransportationId(request.scheduleTransportationId);

        rescheduleReason = rescheduleReasonService.save(rescheduleReason);
        return rescheduleReason.getId();
    }

    @Builder
    public record Request(@NotNull UUID reasonId, @NotNull UUID scheduleTransportationId) {

    }
}
