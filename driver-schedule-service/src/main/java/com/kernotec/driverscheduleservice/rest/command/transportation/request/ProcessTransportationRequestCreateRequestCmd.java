package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseMapper;
import com.kernotec.driverscheduleservice.util.AuthUtil;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessTransportationRequestCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessTransportationRequestCreateRequestCmd.Request, UUID>
{

    private final LocationService locationService;
    private final TransportationRequestStateService transportationRequestStateService;
    private final TransportationRequestService transportationRequestService;

    private final TransportationRequestResponseMapper transportationRequestResponseMapper;

    private final TransportationRequestCreateCmd transportationRequestCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final AuthUtil authUtil;

    @Override
    protected UUID run(Request request) {
        TransportationRequestCreateRequest transportationRequestCreateRequest = request.transportationRequestCreateRequest;

        Coordinate startCoordinate = locationService.getCoordinateOfList(
            transportationRequestCreateRequest.getStartingCoordinates());

        Coordinate endCoordinate = locationService.getCoordinateOfList(
            transportationRequestCreateRequest.getEndCoordinates());

        UUID transportationRequestStateRequestedId = transportationRequestStateService.findIdByCodeThrow(
            TransportationRequestStateEnum.REQUESTED);

        UUID personId = authUtil.getPersonIdFromAuthenticationThrow(request.authentication);

        UUID transportationRequestId = transportationRequestCreateCmd.withRequest(
                TransportationRequestCreateCmd.Request.builder()
                    .startingCoordinate(startCoordinate)
                    .endCoordinate(endCoordinate)
                    .peopleNumber(transportationRequestCreateRequest.getPeopleNumber())
                    .startTime(transportationRequestCreateRequest.getStartTime())
                    .endTime(transportationRequestCreateRequest.getEndTime())
                    .tripType(transportationRequestCreateRequest.getTripType())
                    .transportationRequestStateId(transportationRequestStateRequestedId)
                    .passengers(transportationRequestCreateRequest.getPassengers())
                    .assets(transportationRequestCreateRequest.getAssets())
                    .personRequestId(personId)
                    .build())
            .execute();

        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            transportationRequestId);

        webSocketHandler.emitMessage(
            WebSocketTopic.TRANSPORTATION_REQUEST_CREATED,
            WebSocketSingleResponse.<TransportationRequestResponse>builder()
                .topic(WebSocketTopic.TRANSPORTATION_REQUEST_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(transportationRequestResponseMapper.toResponse(transportationRequest))
                .build()
        );

        return transportationRequestId;
    }

    @Builder
    public record Request(
        @NotNull TransportationRequestCreateRequest transportationRequestCreateRequest,
        @NotNull Authentication authentication)
    {

    }
}
