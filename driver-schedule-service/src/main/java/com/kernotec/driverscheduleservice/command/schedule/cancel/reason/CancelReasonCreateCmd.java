package com.kernotec.driverscheduleservice.command.schedule.cancel.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.schedule.CancelReason;
import com.kernotec.driverscheduleservice.jpa.service.schedule.CancelReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<CancelReasonCreateCmd.Request, UUID>
{

    private final CancelReasonService cancelReasonService;

    @Override
    protected UUID run(Request request) {
        var cancelReason = new CancelReason();

        cancelReason.setOtherReason(request.otherReason);
        cancelReason.setReasonId(request.reasonId);
        cancelReason.setScheduleTransportationId(request.scheduleTransportationId);

        cancelReason = cancelReasonService.save(cancelReason);
        return cancelReason.getId();
    }

    @Builder
    public record Request(String otherReason, @NotNull UUID reasonId,
                          @NotNull UUID scheduleTransportationId)
    {

    }
}
