package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.location.RequestLocationCreateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.LocationTypeEnum;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
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
    private final RequestLocationCreateCmd requestLocationCreateCmd;
    private final TransportationRequestValidationCmd transportationRequestValidationCmd;

    @Override
    protected void validate(Request request) {
        TransportationRequestCreateRequest transportationRequestCreateRequest = request.transportationRequestCreateRequest;

        transportationRequestValidationCmd.withRequest(
                TransportationRequestValidationCmd.Request.builder()
                    .fromDate(transportationRequestCreateRequest.getStartTime())
                    .toDate(transportationRequestCreateRequest.getEndTime())
                    .requestedDate(transportationRequestCreateRequest.getRequestedDate())
                    .zoneId(transportationRequestCreateRequest.getZoneId())
                    .build())
            .execute();
    }

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

        ZonedDateTime startTime = transportationRequestCreateRequest.getStartTime();
        ZonedDateTime startTimeAdjust = startTime.withSecond(0)
            .withNano(0);

        ZonedDateTime endTime = transportationRequestCreateRequest.getEndTime();
        ZonedDateTime endTimeAdjust = endTime.withSecond(0)
            .withNano(0);

        UUID transportationRequestId = transportationRequestCreateCmd.withRequest(
                TransportationRequestCreateCmd.Request.builder()
                    .startingCoordinate(startCoordinate)
                    .endCoordinate(endCoordinate)
                    .peopleNumber(transportationRequestCreateRequest.getPeopleNumber())
                    .startTime(startTimeAdjust)
                    .endTime(endTimeAdjust)
                    .requestedDate(transportationRequestCreateRequest.getRequestedDate())
                    .tripType(transportationRequestCreateRequest.getTripType())
                    .isShortNotice(transportationRequestCreateRequest.getIsShortNotice())
                    .transportationRequestStateId(transportationRequestStateRequestedId)
                    .passengers(transportationRequestCreateRequest.getPassengers())
                    .assets(transportationRequestCreateRequest.getAssets())
                    .personRequestId(personId)
                    .build())
            .execute();

        registryRequestLocation(
            transportationRequestCreateRequest.getLocationStartId(), transportationRequestId,
            LocationTypeEnum.START
        );

        registryRequestLocation(
            transportationRequestCreateRequest.getLocationEndId(), transportationRequestId,
            LocationTypeEnum.END
        );

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

    private void registryRequestLocation(UUID locationId, UUID transportationRequestId,
        LocationTypeEnum locationType)
    {
        if (locationId == null || transportationRequestId == null) {
            log.debug("Location ID or Transportation Request ID is null, skipping registry.");
            return;
        }

        requestLocationCreateCmd.withRequest(RequestLocationCreateCmd.Request.builder()
                .locationId(locationId)
                .transportationRequestId(transportationRequestId)
                .locationType(locationType)
                .build())
            .execute();
    }

    @Builder
    public record Request(
        @NotNull TransportationRequestCreateRequest transportationRequestCreateRequest,
        @NotNull Authentication authentication)
    {

    }
}
