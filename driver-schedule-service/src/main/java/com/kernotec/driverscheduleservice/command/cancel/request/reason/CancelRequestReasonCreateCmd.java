package com.kernotec.driverscheduleservice.command.cancel.request.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.CancelRequestReason;
import com.kernotec.driverscheduleservice.jpa.service.CancelRequestReasonService;
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

        cancelRequestReason.setReasonId(request.reasonId);
        cancelRequestReason.setTransportationRequestId(request.transportationRequestId);

        cancelRequestReason = cancelRequestReasonService.save(cancelRequestReason);
        return cancelRequestReason.getId();
    }

    @Builder
    public record Request(@NotNull UUID reasonId, @NotNull UUID transportationRequestId) {

    }
}
