package com.kernotec.driverschedule.service.command.request.reject.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.request.jpa.entity.RejectReason;
import com.kernotec.driverschedule.service.request.jpa.service.RejectReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RejectReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<RejectReasonCreateCmd.Request, UUID>
{

    private final RejectReasonService rejectReasonService;

    @Override
    protected UUID run(Request request) {
        var rejectReason = new RejectReason();

        rejectReason.setOtherReason(request.otherReason);
        rejectReason.setReasonId(request.reasonId);
        rejectReason.setTransportationRequestId(request.transportationRequestId);

        rejectReason = rejectReasonService.save(rejectReason);
        return rejectReason.getId();
    }

    @Builder
    public record Request(String otherReason, @NotNull UUID reasonId,
                          @NotNull UUID transportationRequestId)
    {

    }
}
