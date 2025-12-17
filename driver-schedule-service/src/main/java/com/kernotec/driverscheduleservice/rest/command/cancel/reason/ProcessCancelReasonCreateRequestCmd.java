package com.kernotec.driverscheduleservice.rest.command.cancel.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.cancel.reason.CancelReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.reason.ReasonCreateCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.cancel.reason.CancelReasonRequest;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessCancelReasonCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessCancelReasonCreateRequestCmd.Request, UUID>
{

    private final ReasonCreateCmd reasonCreateCmd;
    private final CancelReasonCreateCmd cancelReasonCreateCmd;

    @Override
    protected UUID run(Request request) {
        CancelReasonRequest cancelReasonRequest = request.cancelReasonRequest;

        if (cancelReasonRequest.getReasonDescription() == null) {
            return null;
        }

        UUID reasonId = reasonCreateCmd.withRequest(ReasonCreateCmd.Request.builder()
                .reasonDescription(cancelReasonRequest.getReasonDescription())
                .build())
            .execute();

        return cancelReasonCreateCmd.withRequest(CancelReasonCreateCmd.Request.builder()
                .reasonId(reasonId)
                .scheduleTransportationId(cancelReasonRequest.getScheduleTransportationId())
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull CancelReasonRequest cancelReasonRequest) {

    }
}
