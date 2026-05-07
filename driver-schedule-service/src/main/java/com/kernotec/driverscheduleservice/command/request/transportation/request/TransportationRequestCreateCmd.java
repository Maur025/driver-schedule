package com.kernotec.driverscheduleservice.command.request.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.request.jpa.enums.GenerateCodeEnum;
import com.kernotec.driverscheduleservice.request.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.request.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.util.CodeGeneratorUtil;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestCreateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestCreateCmd.Request, UUID>
{

    private final TransportationRequestService transportationRequestService;
    private final CodeGeneratorUtil codeGeneratorUtil;

    @Override
    protected UUID run(Request request) {
        var transportationRequest = new TransportationRequest();

        transportationRequest.setPeopleNumber(request.peopleNumber);
        transportationRequest.setAssets(request.assets);
        transportationRequest.setPassengers(request.passengers);
        transportationRequest.setStartTime(request.startTime);
        transportationRequest.setEndTime(request.endTime);
        transportationRequest.setRequestedDate(request.requestedDate);
        transportationRequest.setRequestedFrom(request.requestedFrom);
        transportationRequest.setRequestedTo(request.requestedTo);
        transportationRequest.setTripType(request.tripType);
        transportationRequest.setShortNotice(
            request.isShortNotice != null && request.isShortNotice);
        transportationRequest.setDetail(request.detail);
        transportationRequest.setAssetPickup(
            request.isAssetPickup != null && request.isAssetPickup);
        transportationRequest.setEstimatedTotalDistanceKm(request.estimatedTotalDistanceKm);
        transportationRequest.setEstimatedTotalDurationMin(request.estimatedTotalDurationMin);
        transportationRequest.setWasRequestedByScheduler(request.wasRequestedByScheduler);
        transportationRequest.setCode(codeGeneratorUtil.generateCodeApp(getGenerateCodeEnum(
            transportationRequest.isAssetPickup(),
            transportationRequest.isWasRequestedByScheduler()
        )));
        transportationRequest.setTransportationRequestStateId(request.transportationRequestStateId);
        transportationRequest.setPersonRequestedId(request.personRequestId);

        transportationRequest = transportationRequestService.save(transportationRequest);
        return transportationRequest.getId();
    }


    private GenerateCodeEnum getGenerateCodeEnum(boolean isAssetPickup,
        boolean wasRequestedByScheduler)
    {
        if (isAssetPickup) {
            return GenerateCodeEnum.ASSET_PICKUP;
        }

        if (wasRequestedByScheduler) {
            return GenerateCodeEnum.REQUEST_BY_SCHEDULER;
        }

        return GenerateCodeEnum.REQUEST;
    }

    @Builder
    public record Request(@NotNull String peopleNumber, String assets, String passengers,
                          @NotNull ZonedDateTime startTime, @NotNull ZonedDateTime endTime,
                          @NotNull ZonedDateTime requestedDate,
                          @NotNull ZonedDateTime requestedFrom, @NotNull ZonedDateTime requestedTo,
                          @NotNull TripTypeEnum tripType, Boolean isShortNotice, String detail,
                          Boolean isAssetPickup, Double estimatedTotalDistanceKm,
                          Double estimatedTotalDurationMin,
                          @NotNull Boolean wasRequestedByScheduler,
                          @NotNull UUID transportationRequestStateId, @NotNull UUID personRequestId)
    {

    }
}
