package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.ReasonService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.rest.dto.request.reject.reason.RejectReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.reason.ReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestRejectedCmd extends
    AbstractCommand<ProcessTransportationRequestRejectedCmd.Request, Void>
{

    private final TransportationRequestService transportationRequestService;
    private final ReasonService reasonService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;
    private final ReasonResponseMapper reasonResponseMapper;

    private final TransportationRequestRejectedCmd transportationRequestRejectedCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected Void run(Request request) {
        transportationRequestRejectedCmd.withRequest(
                TransportationRequestRejectedCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .rejectReasonRequest(request.rejectReasonRequest)
                    .build())
            .execute();

        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            request.transportationRequestId);

        Set<Reason> reasonSet = reasonService.findRejectByTransportationRequestId(
            request.transportationRequestId);

        TransportationRequestResponse transportationRequestResponse = transportationRequestResponseMapper.toResponse(
            transportationRequest);

        if (transportationRequestResponse.getRejectReasons()
            .isEmpty())
        {
            transportationRequestResponse.setRejectReasons(
                reasonResponseMapper.toResponse(reasonSet));
        }

        webSocketHandler.emitMessage(
            WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED,
            WebSocketSingleResponse.<TransportationRequestResponse>builder()
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED)
                .timestamp(ZonedDateTime.now())
                .data(transportationRequestResponse)
                .build()
        );

        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull RejectReasonRequest rejectReasonRequest)
    {

    }
}
