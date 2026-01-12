package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.cancel.request.reason.CancelRequestReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.reason.ReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverscheduleservice.exception.TransportationRequestException;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.dto.TransportationRequestStateDto;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.cancel.request.reason.CancelRequestReasonRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestCancelledCmd extends
    AbstractTransactionalRequiredCommand<ProcessTransportationRequestCancelledCmd.Request, Void>
{

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final TransportationRequestStateService transportationRequestStateService;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final ReasonCreateCmd reasonCreateCmd;
    private final CancelRequestReasonCreateCmd cancelRequestReasonCreateCmd;
    private final TransportationRequestService transportationRequestService;
    private final WebSocketHandler webSocketHandler;
    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    @Override
    protected void validate(Request request) {
        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .build())
            .execute();

        TransportationRequestStateDto transportationRequestStateDto = transportationRequestDto.getTransportationRequestState();

        if (!TransportationRequestStateEnum.REQUESTED.equals(
            TransportationRequestStateEnum.fromValue(transportationRequestStateDto.getCode())))
        {
            throw new TransportationRequestException(
                "state.invalid.to.cancel", "'" + transportationRequestStateDto.getCode() + "'",
                HttpStatus.BAD_REQUEST.value()
            );
        }
    }

    @Override
    protected Void run(Request request) {
        CancelRequestReasonRequest cancelRequestReasonRequest = request.cancelRequestReasonRequest;

        UUID transportationRequestStateCancelledId = transportationRequestStateService.findIdByCodeThrow(
            TransportationRequestStateEnum.CANCELLED);

        transportationRequestUpdateCmd.withRequest(TransportationRequestUpdateCmd.Request.builder()
                .transportationRequestId(request.transportationRequestId)
                .transportationRequestStateId(transportationRequestStateCancelledId)
                .build())
            .execute();

        registerCancelReason(
            request.transportationRequestId,
            cancelRequestReasonRequest.getReasonDescription()
        );

        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            request.transportationRequestId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRANSPORTATION_REQUEST_CANCELLED,
            WebSocketSingleResponse.<TransportationRequestResponse>builder()
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CANCELLED)
                .timestamp(ZonedDateTime.now())
                .data(transportationRequestResponseMapper.toResponse(transportationRequest))
                .build()
        );

        return null;
    }

    private void registerCancelReason(UUID transportationRequestId, String reasonDescription) {
        if (reasonDescription == null || reasonDescription.isBlank()) {
            log.debug("No cancel reason description provided, skipping reason creation.");
            return;
        }

        UUID reasonId = reasonCreateCmd.withRequest(ReasonCreateCmd.Request.builder()
                .reasonDescription(reasonDescription)
                .build())
            .execute();

        cancelRequestReasonCreateCmd.withRequest(CancelRequestReasonCreateCmd.Request.builder()
                .transportationRequestId(transportationRequestId)
                .reasonId(reasonId)
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull CancelRequestReasonRequest cancelRequestReasonRequest)
    {

    }
}
