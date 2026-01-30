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
public class ReasonCreateCmd extends
    AbstractTransactionalRequiredCommand<ReasonCreateCmd.Request, UUID>
{

    private final ReasonService reasonService;

    @Override
    protected UUID run(Request request) {
        var reason = new Reason();

        // reason.setReasonDescription(request.reasonDescription);

        reason = reasonService.save(reason);
        return reason.getId();
    }

    @Builder
    public record Request(@NotNull String reasonDescription) {

    }
}
