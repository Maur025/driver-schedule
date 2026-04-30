package com.kernotec.driverscheduleservice.rest.command.request;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestService;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.service.NotificationOrchestrator;
import com.kernotec.driverscheduleservice.notification.templates.RequestCreateNotificationTemplate;
import com.kernotec.driverscheduleservice.rest.dto.request.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.socket.request.TransportationRequestSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestCreateRequestCmd extends
    AbstractCommand<ProcessTransportationRequestCreateRequestCmd.Request, TransportationRequest>
{

    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestFlowCreateCmd transportationRequestFlowCreateCmd;
    private final TransportationRequestSocketHandler transportationRequestSocketHandler;
    private final NotificationOrchestrator notificationOrchestrator;

    @Override
    protected TransportationRequest run(Request request) {
        UUID transportationRequestId = transportationRequestFlowCreateCmd.withRequest(
                TransportationRequestFlowCreateCmd.Request.builder()
                    .transportationRequestCreateRequest(request.transportationRequestCreateRequest)
                    .build())
            .execute();

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(transportationRequestId)
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CREATED)
                .build());

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(RequestCreateNotificationTemplate.TITLE)
            .body(RequestCreateNotificationTemplate.BODY)
            .campaignRecipient(RequestCreateNotificationTemplate.RECIPIENT)
            .dataMap(Map.of("screen", "request/" + transportationRequestId))
            .build());

        return transportationRequestService.findByIdThrow(transportationRequestId);
    }


    @Builder
    public record Request(
        @NotNull @Valid TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
