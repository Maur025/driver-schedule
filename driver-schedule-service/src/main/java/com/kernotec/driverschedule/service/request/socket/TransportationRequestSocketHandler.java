package com.kernotec.driverschedule.service.request.socket;

import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestService;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.service.request.rest.mapper.response.TransportationRequestResponseMapper;
import com.kernotec.driverschedule.service.rest.socket.SocketHandler;
import com.kernotec.driverschedule.service.web.socket.WebSocketHandler;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import org.springframework.stereotype.Service;

@Service
public class TransportationRequestSocketHandler extends
    SocketHandler<TransportationRequestSocketHandler.Request, TransportationRequestResponse>
{

    private final TransportationRequestService transportationRequestService;
    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    public TransportationRequestSocketHandler(WebSocketHandler webSocketHandler,
        TransportationRequestService transportationRequestService,
        TransportationRequestResponseMapper transportationRequestResponseMapper)
    {
        super(webSocketHandler);
        this.transportationRequestService = transportationRequestService;
        this.transportationRequestResponseMapper = transportationRequestResponseMapper;
    }

    @Override
    protected String getTopic(Request request) {
        return request.topic();
    }

    @Override
    protected Set<UUID> getToList(Request request) {
        return request.toList();
    }

    @Override
    protected TransportationRequestResponse getResponseData(Request request) {
        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            request.transportationRequestId());

        return transportationRequestResponseMapper.toResponse(transportationRequest);
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId, @NotNull String topic,
                          Set<UUID> toList)
    {

    }
}
