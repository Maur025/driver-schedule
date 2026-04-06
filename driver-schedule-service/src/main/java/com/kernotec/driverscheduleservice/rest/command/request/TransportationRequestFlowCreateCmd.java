package com.kernotec.driverscheduleservice.rest.command.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.request.coord.RequestCoordManyCreateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestCreateCmd;
import com.kernotec.driverscheduleservice.common.security.SecurityAuthProvider;
import com.kernotec.driverscheduleservice.jpa.entity.request.RequestCoord;
import com.kernotec.driverscheduleservice.jpa.enums.request.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.request.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.rest.dto.request.request.request.coord.RequestCoordCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.request.transportation.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.rest.mapper.request.request.coord.RequestCoordEntityMapper;
import com.kernotec.driverscheduleservice.util.CommonUtil;
import com.kernotec.driverscheduleservice.util.ZonedDateTimeUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransportationRequestFlowCreateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestFlowCreateCmd.Request, UUID>
{

    private final TransportationRequestStateService transportationRequestStateService;

    private final TransportationRequestValidationCmd transportationRequestValidationCmd;
    private final TransportationRequestCreateCmd transportationRequestCreateCmd;
    private final RequestCoordEntityMapper requestCoordEntityMapper;
    private final RequestCoordManyCreateCmd requestCoordManyCreateCmd;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final SecurityAuthProvider securityAuthProvider;
    private final PersonService personService;

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

        UUID personId = getPersonId(request.transportationRequestCreateRequest);

        ZonedDateTime startTime = transportationRequestCreateRequest.getStartTime();
        ZonedDateTime startTimeAdjust = startTime.withSecond(0)
            .withNano(0);

        ZonedDateTime endTime = transportationRequestCreateRequest.getEndTime();
        ZonedDateTime endTimeAdjust = endTime.withSecond(0)
            .withNano(0);

        ZonedDateTime requestedFrom = zonedDateTimeUtil.getNewOfDateAndTime(
            transportationRequestCreateRequest.getRequestedDate(), startTime,
            transportationRequestCreateRequest.getZoneId()
        );

        ZonedDateTime requestedTo = zonedDateTimeUtil.getNewOfDateAndTime(
            transportationRequestCreateRequest.getRequestedDate(), endTime,
            transportationRequestCreateRequest.getZoneId()
        );

        UUID transportationRequestId = transportationRequestCreateCmd.withRequest(
                TransportationRequestCreateCmd.Request.builder()
                    .peopleNumber(transportationRequestCreateRequest.getPeopleNumber())
                    .assets(CommonUtil.toUpperCase(transportationRequestCreateRequest.getAssets()))
                    .passengers(
                        CommonUtil.toUpperCase(transportationRequestCreateRequest.getPassengers()))
                    .startTime(startTimeAdjust)
                    .endTime(endTimeAdjust)
                    .requestedDate(transportationRequestCreateRequest.getRequestedDate())
                    .requestedFrom(requestedFrom)
                    .requestedTo(requestedTo)
                    .tripType(transportationRequestCreateRequest.getTripType())
                    .isShortNotice(transportationRequestCreateRequest.getIsShortNotice())
                    .detail(CommonUtil.toUpperCase(transportationRequestCreateRequest.getDetail()))
                    .isAssetPickup(transportationRequestCreateRequest.getIsAssetPickup())
                    .estimatedTotalDistanceKm(
                        transportationRequestCreateRequest.getEstimatedTotalDistanceKm())
                    .estimatedTotalDurationMin(
                        transportationRequestCreateRequest.getEstimatedTotalDurationMin())
                    .wasRequestedByScheduler(
                        transportationRequestCreateRequest.getPersonRequestedId() != null)
                    .transportationRequestStateId(transportationRequestStateRequestedId)
                    .personRequestId(personId)
                    .build())
            .execute();

        registryRequestCoords(
            transportationRequestCreateRequest.getRequestCoords(), transportationRequestId);

        return transportationRequestId;
    }

    private UUID getPersonId(TransportationRequestCreateRequest request)
    {
        if (request.getPersonRequestedId() != null) {
            return request.getPersonRequestedId();
        }

        return personService.findIdByUserIdAuthenticateThrow();
    }

    private void registryRequestCoords(
        List<RequestCoordCreateRequest> requestCoordCreateRequestList, UUID transportationRequestId)
    {
        if (requestCoordCreateRequestList == null || requestCoordCreateRequestList.isEmpty()) {
            log.debug("No coordinates to register for the transportation request12.");
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
        @NotNull TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
