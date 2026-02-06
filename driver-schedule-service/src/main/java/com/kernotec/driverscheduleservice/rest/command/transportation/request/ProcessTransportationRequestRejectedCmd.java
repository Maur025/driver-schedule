package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.ReasonService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.rest.dto.request.reject.reason.RejectReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.reason.ReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    @Override
    protected Void run(Request request) {
        transportationRequestRejectedCmd.withRequest(
                TransportationRequestRejectedCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .rejectReasonRequest(request.rejectReasonRequest)
                    .build())
            .execute();

        TransportationRequestResponse transportationRequestResponse = getTransportationRequestResponseWithFix(
            request.transportationRequestId);

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .transportationRequestStateId(
                        transportationRequestResponse.getTransportationRequestStateId())
                    .build())
            .execute();

        emitSocketMessage(request.transportationRequestId, transportationRequestResponse);

        return null;
    }

    private void emitSocketMessage(UUID transportationRequestId,
        TransportationRequestResponse transportationRequestResponse)
    {
        var socketResponse = WebSocketSingleResponse.<TransportationRequestResponse>builder()
            .timestamp(ZonedDateTime.now())
            .data(transportationRequestResponse);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED,
            socketResponse.topic(WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED)
                .build()
        );

        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .build())
            .execute();

        webSocketHandler.emitMessageToUser(
            transportationRequestDto.getPersonRequested()
                .getUserId(), WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED_TO_USER,
            socketResponse.topic(WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED_TO_USER)
                .build()
        );
    }

    /* TODO: Review util, remove because reason structure changed */
    private TransportationRequestResponse getTransportationRequestResponseWithFix(
        UUID transportationRequestId)
    {
        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            transportationRequestId);

        /*Set<Reason> reasonSet = reasonService.findRejectByTransportationRequestId(
            transportationRequestId);*/

        TransportationRequestResponse transportationRequestResponse = transportationRequestResponseMapper.toResponse(
            transportationRequest);

        if (transportationRequestResponse.getRejectReasons()
            .isEmpty())
        {
            /*transportationRequestResponse.setRejectReasons(
                reasonResponseMapper.toResponse(reasonSet));*/
        }

        return transportationRequestResponse;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull @Valid RejectReasonRequest rejectReasonRequest)
    {

    }
}
