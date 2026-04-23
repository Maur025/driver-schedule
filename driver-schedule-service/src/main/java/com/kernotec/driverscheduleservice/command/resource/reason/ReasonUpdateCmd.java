package com.kernotec.driverscheduleservice.command.resource.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Reason;
import com.kernotec.driverscheduleservice.jpa.service.resource.ReasonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReasonUpdateCmd extends
    AbstractTransactionalRequiredCommand<ReasonUpdateCmd.Request, Void>
{

    private final ReasonService reasonService;

    @Override
    protected Void run(Request request) {
        Reason reason = reasonService.findByIdThrow(request.reasonId);

        if (request.value != null) {
            reason.setValue(request.value);
        }
        if (request.code != null) {
            reason.setCode(request.code);
        }
        if (request.reasonTypeId != null) {
            reason.setReasonTypeId(request.reasonTypeId);
        }

        reasonService.save(reason);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID reasonId, String value, String code, UUID reasonTypeId) {

    }
}
