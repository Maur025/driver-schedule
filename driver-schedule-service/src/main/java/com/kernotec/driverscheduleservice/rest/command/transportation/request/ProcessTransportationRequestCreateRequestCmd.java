package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestCreateRequestCmd extends
    AbstractCommand<ProcessTransportationRequestCreateRequestCmd.Request, TransportationRequest>
{

    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    private final TransportationRequestApproveCmd transportationRequestApproveCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected TransportationRequest run(Request request) {
        UUID transportationRequestId = transportationRequestApproveCmd.withRequest(
                TransportationRequestApproveCmd.Request.builder()
                    .transportationRequestCreateRequest(request.transportationRequestCreateRequest)
                    .authentication(request.authentication)
                    .build())
            .execute();

        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            transportationRequestId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRANSPORTATION_REQUEST_CREATED,
            WebSocketSingleResponse.<TransportationRequestResponse>builder()
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(transportationRequestResponseMapper.toResponse(transportationRequest))
                .build()
        );

        return transportationRequest;
    }


    @Builder
    public record Request(
        @NotNull TransportationRequestCreateRequest transportationRequestCreateRequest,
        @NotNull Authentication authentication)
    {

    }
}
