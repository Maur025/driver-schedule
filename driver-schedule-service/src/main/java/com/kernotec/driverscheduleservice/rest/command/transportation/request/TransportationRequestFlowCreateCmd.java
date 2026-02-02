package com.kernotec.driverscheduleservice.rest.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.coord.RequestCoordManyCreateCmd;
import com.kernotec.driverscheduleservice.command.transportation.request.TransportationRequestCreateCmd;
import com.kernotec.driverscheduleservice.jpa.entity.RequestCoord;
import com.kernotec.driverscheduleservice.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.request.coord.RequestCoordCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.mapper.request.request.coord.RequestCoordEntityMapper;
import com.kernotec.driverscheduleservice.util.AuthUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransportationRequestFlowCreateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestFlowCreateCmd.Request, UUID>
{

    private final TransportationRequestStateService transportationRequestStateService;
    private final AuthUtil authUtil;

    private final TransportationRequestValidationCmd transportationRequestValidationCmd;
    private final TransportationRequestCreateCmd transportationRequestCreateCmd;
    private final RequestCoordEntityMapper requestCoordEntityMapper;
    private final RequestCoordManyCreateCmd requestCoordManyCreateCmd;

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

        UUID transportationRequestStateRequestedId = transportationRequestStateService.findIdByCodeThrow(
            TransportationRequestStateEnum.REQUESTED);

        UUID personId = getPersonId(
            request.transportationRequestCreateRequest, request.authentication);

        ZonedDateTime startTime = transportationRequestCreateRequest.getStartTime();
        ZonedDateTime startTimeAdjust = startTime.withSecond(0)
            .withNano(0);

        ZonedDateTime endTime = transportationRequestCreateRequest.getEndTime();
        ZonedDateTime endTimeAdjust = endTime.withSecond(0)
            .withNano(0);

        UUID transportationRequestId = transportationRequestCreateCmd.withRequest(
                TransportationRequestCreateCmd.Request.builder()
                    .peopleNumber(transportationRequestCreateRequest.getPeopleNumber())
                    .assets(transportationRequestCreateRequest.getAssets())
                    .passengers(transportationRequestCreateRequest.getPassengers())
                    .startTime(startTimeAdjust)
                    .endTime(endTimeAdjust)
                    .requestedDate(transportationRequestCreateRequest.getRequestedDate())
                    .tripType(transportationRequestCreateRequest.getTripType())
                    .isShortNotice(transportationRequestCreateRequest.getIsShortNotice())
                    .detail(transportationRequestCreateRequest.getDetail())
                    .isAssetPickup(transportationRequestCreateRequest.getIsAssetPickup())
                    .estimatedTotalDistanceKm(
                        transportationRequestCreateRequest.getEstimatedTotalDistanceKm())
                    .estimatedTotalDurationMin(
                        transportationRequestCreateRequest.getEstimatedTotalDurationMin())
                    .transportationRequestStateId(transportationRequestStateRequestedId)
                    .personRequestId(personId)
                    .build())
            .execute();

        registryRequestCoords(
            transportationRequestCreateRequest.getRequestCoords(), transportationRequestId);

        return transportationRequestId;
    }

    private UUID getPersonId(TransportationRequestCreateRequest request,
        Authentication authentication)
    {
        if (request.getPersonRequestedId() != null) {
            return request.getPersonRequestedId();
        }

        return authUtil.getPersonIdFromAuthenticationThrow(authentication);
    }

    private void registryRequestCoords(
        List<RequestCoordCreateRequest> requestCoordCreateRequestList, UUID transportationRequestId)
    {
        if (requestCoordCreateRequestList == null || requestCoordCreateRequestList.isEmpty()) {
            log.debug("No coordinates to register for the transportation request.");
            return;
        }

        List<RequestCoord> requestCoordListToSave = requestCoordEntityMapper.toEntity(
            requestCoordCreateRequestList, transportationRequestId);

        requestCoordManyCreateCmd.withRequest(RequestCoordManyCreateCmd.Request.builder()
                .requestCoordList(requestCoordListToSave)
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
