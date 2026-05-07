package com.kernotec.driverschedule.service.command.resource.reason;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.resource.Reason;
import com.kernotec.driverschedule.service.jpa.service.resource.ReasonService;
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

        reason.setValue(request.value);
        reason.setCode(request.code);
        reason.setReasonTypeId(request.reasonTypeId);

        reason = reasonService.save(reason);
        return reason.getId();
    }

    @Builder
    public record Request(@NotNull String value, @NotNull String code, @NotNull UUID reasonTypeId) {

    }
}
