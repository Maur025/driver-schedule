package com.kernotec.driverscheduleservice.command.request.transportation.request.state;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequestState;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestStateService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestStateCreateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestStateCreateCmd.Request, UUID>
{

    private final TransportationRequestStateService transportationRequestStateService;

    @Override
    protected UUID run(Request request) {
        var transportationRequestState = new TransportationRequestState();

        transportationRequestState.setName(request.name);
        transportationRequestState.setCode(request.code);

        transportationRequestState = transportationRequestStateService.save(
            transportationRequestState);
        return transportationRequestState.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String code) {

    }
}
