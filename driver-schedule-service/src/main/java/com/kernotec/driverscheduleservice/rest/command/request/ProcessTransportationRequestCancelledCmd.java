package com.kernotec.driverscheduleservice.rest.command.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.cancel.request.reason.CancelRequestReasonCreateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.exception.request.TransportationRequestException;
import com.kernotec.driverscheduleservice.jpa.dto.request.TransportationRequestDto;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestService;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.dto.request.request.cancel.request.reason.CancelRequestReasonRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
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

    private final TransportationRequestStateService transportationRequestStateService;
    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final CancelRequestReasonCreateCmd cancelRequestReasonCreateCmd;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final PersonService personService;

    @Override
    protected void validate(Request request) {
        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .build())
            .execute();

        UUID currentPersonLoggedId = personService.findIdByUserIdAuthenticateThrow();

        if (currentPersonLoggedId != transportationRequestDto.getPersonRequestedId()) {
            throw new TransportationRequestException(
                "request.not.belong", "", HttpStatus.CONFLICT.value());
        }

        TransportationRequestStateEnum requestStateCurrent = TransportationRequestStateEnum.fromValue(
            transportationRequestDto.getTransportationRequestState()
                .getCode());

        if (!requestStateCurrent.canTransitionTo(TransportationRequestStateEnum.CANCELLED)) {
            throw new TransportationRequestException(
                "state.invalid.to.cancel",
                "'" + requestStateCurrent + "'", HttpStatus.BAD_REQUEST.value()
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

        cancelRequestReasonCreateCmd.withRequest(CancelRequestReasonCreateCmd.Request.builder()
                .transportationRequestId(request.transportationRequestId)
                .reasonId(cancelRequestReasonRequest.getReasonId())
                .otherReason(cancelRequestReasonRequest.getOtherReason())
                .build())
            .execute();

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .transportationRequestStateId(transportationRequestStateCancelledId)
                    .build())
            .execute();

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

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull @Valid CancelRequestReasonRequest cancelRequestReasonRequest)
    {

    }
}
