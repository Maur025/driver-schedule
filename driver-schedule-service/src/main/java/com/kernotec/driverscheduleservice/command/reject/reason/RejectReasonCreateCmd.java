package com.kernotec.driverscheduleservice.command.reject.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.RejectReason;
import com.kernotec.driverscheduleservice.jpa.service.RejectReasonService;
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

        rejectReason.setReasonId(request.reasonId);
        rejectReason.setTransportationRequestId(request.transportationRequestId);

        rejectReason = rejectReasonService.save(rejectReason);
        return rejectReason.getId();
    }

    @Builder
    public record Request(@NotNull UUID reasonId, @NotNull UUID transportationRequestId) {

    }
}
