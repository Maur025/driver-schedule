package com.kernotec.driverscheduleservice.command.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.jpa.service.ReasonService;
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

        if (request.reasonDescription != null) {
            // reason.setReasonDescription(request.reasonDescription);
        }

        reasonService.save(reason);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID reasonId, String reasonDescription) {

    }
}
