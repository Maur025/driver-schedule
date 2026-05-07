package com.kernotec.driverscheduleservice.request.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.request.request.coord.RequestCoordManyCreateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.TransportationRequestCreateCmd;
import com.kernotec.driverscheduleservice.command.request.transportation.request.log.TransportationRequestLogCreateCmd;
import com.kernotec.driverscheduleservice.request.jpa.entity.RequestCoord;
import com.kernotec.driverscheduleservice.request.jpa.enums.TransportationRequestStateEnum;
import com.kernotec.driverscheduleservice.request.jpa.service.TransportationRequestStateService;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.request.rest.dto.request.RequestCoordCreateRequest;
import com.kernotec.driverscheduleservice.request.rest.dto.request.TransportationRequestCreateRequest;
import com.kernotec.driverscheduleservice.request.rest.mapper.request.RequestCoordEntityMapper;
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
    private final PersonService personService;

    private final RequestCoordEntityMapper requestCoordEntityMapper;

    private final TransportationRequestValidationCmd transportationRequestValidationCmd;
    private final TransportationRequestCreateCmd transportationRequestCreateCmd;
    private final RequestCoordManyCreateCmd requestCoordManyCreateCmd;
    private final ZonedDateTimeUtil zonedDateTimeUtil;
    private final TransportationRequestLogCreateCmd transportationRequestLogCreateCmd;

    @Override
    protected void validate(Request request) {
        TransportationRequestCreateRequest transportationRequestCreateRequest = request.transportationRequestCreateRequest;

        transportationRequestValidationCmd.withRequest(
                TransportationRequestValidationCmd.Request.builder()
                    .fromDate(transportationRequestCreateRequest.getStartTime())
                    .toDate(transportationRequestCreateRequest.getEndTime())
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

        ZonedDateTime requestedFrom = zonedDateTimeUtil.getDateScheduleNormalized(
            transportationRequestCreateRequest.getStartTime());

        ZonedDateTime requestedTo = zonedDateTimeUtil.getDateScheduleNormalized(
            transportationRequestCreateRequest.getEndTime());

        UUID transportationRequestId = transportationRequestCreateCmd.withRequest(
                TransportationRequestCreateCmd.Request.builder()
                    .peopleNumber(transportationRequestCreateRequest.getPeopleNumber())
                    .assets(CommonUtil.toUpperCase(transportationRequestCreateRequest.getAssets()))
                    .passengers(
                        CommonUtil.toUpperCase(transportationRequestCreateRequest.getPassengers()))
                    .startTime(requestedFrom)
                    .endTime(requestedTo)
                    .requestedDate(requestedFrom)
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

        transportationRequestLogCreateCmd.withRequest(
                TransportationRequestLogCreateCmd.Request.builder()
                    .transportationRequestId(transportationRequestId)
                    .transportationRequestStateId(transportationRequestStateRequestedId)
                    .build())
            .execute();

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
        @NotNull TransportationRequestCreateRequest transportationRequestCreateRequest)
    {

    }
}
