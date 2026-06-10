package com.kernotec.driverschedule.service.scheduling.rest.command.schedule;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.command.schedule.CancelReasonCreateCmd;
import com.kernotec.driverschedule.service.scheduling.rest.dto.request.schedule.CancelReasonRequest;
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

    private final CancelReasonCreateCmd cancelReasonCreateCmd;

    @Override
    protected UUID run(Request request) {
        CancelReasonRequest cancelReasonRequest = request.cancelReasonRequest;

        return cancelReasonCreateCmd.withRequest(CancelReasonCreateCmd.Request.builder()
                .reasonId(cancelReasonRequest.getReasonId())
                .scheduleTransportationId(cancelReasonRequest.getScheduleTransportationId())
                .otherReason(cancelReasonRequest.getOtherReason())
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull CancelReasonRequest cancelReasonRequest) {

    }
}
