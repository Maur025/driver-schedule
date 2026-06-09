package com.kernotec.driverschedule.service.scheduling.command.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequestLog;
import com.kernotec.driverschedule.service.scheduling.jpa.service.TransportationRequestLogService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestLogCreateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestLogCreateCmd.Request, UUID>
{

    private final TransportationRequestLogService transportationRequestLogService;

    @Override
    protected UUID run(Request request) {
        var transportationRequestLog = new TransportationRequestLog();

        transportationRequestLog.setTransportationRequestId(request.transportationRequestId);
        transportationRequestLog.setTransportationRequestStateId(
            request.transportationRequestStateId);

        transportationRequestLog = transportationRequestLogService.save(transportationRequestLog);
        return transportationRequestLog.getId();
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull UUID transportationRequestStateId)
    {

    }
}
