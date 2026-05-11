package com.kernotec.driverschedule.service.request.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequestState;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestStateUpdateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestStateUpdateCmd.Request, Void>
{

    private final TransportationRequestStateService transportationRequestStateService;

    @Override
    protected Void run(Request request) {
        TransportationRequestState transportationRequestState = transportationRequestStateService.findByIdThrow(
            request.transportationRequestStateId);

        if (request.name != null) {
            transportationRequestState.setName(request.name);
        }

        transportationRequestStateService.save(transportationRequestState);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestStateId, String name) {

    }
}
