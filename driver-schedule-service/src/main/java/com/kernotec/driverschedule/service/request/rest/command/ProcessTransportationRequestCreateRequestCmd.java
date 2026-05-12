package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.jpa.service.TransportationRequestService;
import com.kernotec.driverschedule.service.request.notification.RequestPushNotification;
import com.kernotec.driverschedule.service.request.rest.dto.request.TransportationRequestCreateRequest;
import com.kernotec.driverschedule.service.request.socket.RequestSocketTopic;
import com.kernotec.driverschedule.service.request.socket.TransportationRequestSocketHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private final RequestPushNotification requestPushNotification;

    @Override
    protected TransportationRequest run(Request request) {
        UUID transportationRequestId = transportationRequestFlowCreateCmd.withRequest(
                TransportationRequestFlowCreateCmd.Request.builder()
                    .transportationRequestCreateRequest(request.transportationRequestCreateRequest)
                    .build())
            .execute();

        requestPushNotification.onCreate(transportationRequestId);

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(transportationRequestId)
                .topic(RequestSocketTopic.TRANSPORTATION_REQUEST_CREATED)
                .build());

        return transportationRequestService.findByIdThrow(transportationRequestId);
    }


    @Builder
    public record Request(
        @NotNull @Valid TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
