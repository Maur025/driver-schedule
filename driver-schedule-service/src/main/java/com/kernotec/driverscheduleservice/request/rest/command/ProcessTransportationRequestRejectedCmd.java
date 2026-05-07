package com.kernotec.driverscheduleservice.request.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestGetDtoCmd;
import com.kernotec.driverscheduleservice.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.service.NotificationOrchestrator;
import com.kernotec.driverscheduleservice.notification.templates.NotificationTemplate.RequestRejectedTemplate;
import com.kernotec.driverscheduleservice.request.rest.dto.request.RejectReasonRequest;
import com.kernotec.driverscheduleservice.request.socket.TransportationRequestSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
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

    private final TransportationRequestRejectedCmd transportationRequestRejectedCmd;
    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;
    private final TransportationRequestSocketHandler transportationRequestSocketHandler;
    private final NotificationOrchestrator notificationOrchestrator;

    @Override
    protected Void run(Request request) {
        transportationRequestRejectedCmd.withRequest(
                TransportationRequestRejectedCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId)
                    .rejectReasonRequest(request.rejectReasonRequest)
                    .build())
            .execute();

        TransportationRequestDto transportationRequestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(request.transportationRequestId())
                    .build())
            .execute();

        UUID userToEmit = transportationRequestDto.getPersonRequested()
            .getUserId();

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(RequestRejectedTemplate.TITLE)
            .body(RequestRejectedTemplate.BODY)
            .campaignRecipient(RequestRejectedTemplate.RECEIVER)
            .dataMap(Map.of("screen", "request/" + request.transportationRequestId()))
            .personIds(Set.of(transportationRequestDto.getPersonRequestedId()))
            .build());

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(request.transportationRequestId())
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED)
                .build());

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(request.transportationRequestId())
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_REJECTED_TO_USER)
                .toList(Set.of(userToEmit))
                .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId,
                          @NotNull @Valid RejectReasonRequest rejectReasonRequest)
    {

    }
}
