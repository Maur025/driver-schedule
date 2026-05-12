package com.kernotec.driverschedule.service.request.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.request.command.TransportationRequestGetDtoCmd;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import com.kernotec.driverschedule.service.request.notification.RequestPushNotification;
import com.kernotec.driverschedule.service.request.rest.dto.request.RejectReasonRequest;
import com.kernotec.driverschedule.service.request.socket.RequestSocketTopic;
import com.kernotec.driverschedule.service.request.socket.TransportationRequestSocketHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private final RequestPushNotification requestPushNotification;

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

        requestPushNotification.onRejected(
            request.transportationRequestId(),
            Set.of(transportationRequestDto.getPersonRequestedId())
        );

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(request.transportationRequestId())
                .topic(RequestSocketTopic.TRANSPORTATION_REQUEST_REJECTED)
                .build());

        transportationRequestSocketHandler.emitMessage(
            TransportationRequestSocketHandler.Request.builder()
                .transportationRequestId(request.transportationRequestId())
                .topic(RequestSocketTopic.TRANSPORTATION_REQUEST_REJECTED_TO_USER)
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
