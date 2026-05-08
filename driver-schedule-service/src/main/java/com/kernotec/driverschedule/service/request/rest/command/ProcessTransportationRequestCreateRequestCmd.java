package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.notification.notification.dto.NotificationSendRequest;
import com.kernotec.driverschedule.notification.notification.service.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.notification.templates.NotificationTemplate.RequestCreateTemplate;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestService;
import com.kernotec.driverschedule.service.request.rest.dto.request.TransportationRequestCreateRequest;
import com.kernotec.driverschedule.service.request.socket.TransportationRequestSocketHandler;
import com.kernotec.driverschedule.socket.WebSocketTopic;
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

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(RequestCreateTemplate.TITLE)
            .body(RequestCreateTemplate.BODY)
            .campaignRecipient(RequestCreateTemplate.RECEIVER)
            .dataMap(Map.of("screen", "request/" + transportationRequestId))
            .build());

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(transportationRequestId)
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CREATED)
                .build());

        return transportationRequestService.findByIdThrow(transportationRequestId);
    }


    @Builder
    public record Request(
        @NotNull @Valid TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
