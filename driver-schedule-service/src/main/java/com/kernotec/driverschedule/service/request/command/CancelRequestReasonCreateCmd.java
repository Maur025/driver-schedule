package com.kernotec.driverschedule.service.request.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.request.jpa.entity.CancelRequestReason;
import com.kernotec.driverschedule.service.request.jpa.service.CancelRequestReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CancelRequestReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<CancelRequestReasonCreateCmd.Request, UUID>
{

    private final CancelRequestReasonService cancelRequestReasonService;

    @Override
    protected UUID run(Request request) {
        var cancelRequestReason = new CancelRequestReason();

        cancelRequestReason.setOtherReason(request.otherReason);
        cancelRequestReason.setReasonId(request.reasonId);
        cancelRequestReason.setTransportationRequestId(request.transportationRequestId);

        cancelRequestReason = cancelRequestReasonService.save(cancelRequestReason);
        return cancelRequestReason.getId();
    }

    @Builder
    public record Request(String otherReason, @NotNull UUID reasonId,
                          @NotNull UUID transportationRequestId)
    {

    }
}
