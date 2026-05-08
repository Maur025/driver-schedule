package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.notification.dto.NotificationSendRequest;
import com.kernotec.driverschedule.notification.notification.service.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.notification.templates.NotificationTemplate.RequestCancelledTemplate;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import com.kernotec.driverschedule.service.command.request.cancel.request.reason.CancelRequestReasonCreateCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.TransportationRequestUpdateCmd;
import com.kernotec.driverschedule.service.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverschedule.service.exception.request.TransportationRequestException;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestStateService;
import com.kernotec.driverschedule.service.request.rest.dto.request.CancelRequestReasonRequest;
import com.kernotec.driverschedule.service.request.socket.TransportationRequestSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
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
    private final PersonService personService;

    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final CancelRequestReasonCreateCmd cancelRequestReasonCreateCmd;
    private final TransportationRequestUpdateCmd transportationRequestUpdateCmd;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    private final TransportationRequestSocketHandler transportationRequestSocketHandler;
    private final NotificationOrchestrator notificationOrchestrator;

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

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(RequestCancelledTemplate.TITLE)
            .body(RequestCancelledTemplate.BODY)
            .campaignRecipient(RequestCancelledTemplate.RECEIVER)
            .dataMap(Map.of("screen", "request/" + request.transportationRequestId()))
            .build());

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(request.transportationRequestId())
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CANCELLED)
                .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull @Valid CancelRequestReasonRequest cancelRequestReasonRequest)
    {

    }
}
