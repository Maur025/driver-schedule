package com.kernotec.driverscheduleservice.rest.command.request;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestService;
import com.kernotec.driverscheduleservice.rest.dto.request.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.request.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestCreateRequestCmd extends
    AbstractCommand<ProcessTransportationRequestCreateRequestCmd.Request, TransportationRequest>
{

    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    private final TransportationRequestFlowCreateCmd transportationRequestFlowCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    @Override
    protected TransportationRequest run(Request request) {
        UUID transportationRequestId = transportationRequestFlowCreateCmd.withRequest(
                TransportationRequestFlowCreateCmd.Request.builder()
                    .transportationRequestCreateRequest(request.transportationRequestCreateRequest)
                    .build())
            .execute();

        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            transportationRequestId);

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .transportationRequestStateId(
                        transportationRequest.getTransportationRequestStateId())
                    .build())
            .execute();

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
        @NotNull @Valid TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
