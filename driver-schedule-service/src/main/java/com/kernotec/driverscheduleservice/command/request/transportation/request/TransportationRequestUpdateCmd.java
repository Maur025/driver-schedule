package com.kernotec.driverscheduleservice.command.request.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.request.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.request.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.request.jpa.service.TransportationRequestService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TransportationRequestUpdateCmd extends
    AbstractTransactionalRequiredCommand<TransportationRequestUpdateCmd.Request, Void>
{

    private final TransportationRequestService transportationRequestService;

    @Override
    protected Void run(Request request) {
        TransportationRequest transportationRequest = transportationRequestService.findByIdThrow(
            request.transportationRequestId);

        if (request.peopleNumber != null) {
            transportationRequest.setPeopleNumber(request.peopleNumber);
        }
        if (request.assets != null) {
            transportationRequest.setAssets(request.assets);
        }
        if (request.passengers != null) {
            transportationRequest.setPassengers(request.passengers);
        }
        if (request.startTime != null) {
            transportationRequest.setStartTime(request.startTime);
        }
        if (request.endTime != null) {
            transportationRequest.setEndTime(request.endTime);
        }
        if (request.requestedDate != null) {
            transportationRequest.setRequestedDate(request.requestedDate);
        }
        if (request.requestedFrom != null) {
            transportationRequest.setRequestedFrom(request.requestedFrom);
        }
        if (request.requestedTo != null) {
            transportationRequest.setRequestedTo(request.requestedTo);
        }
        if (request.tripType != null) {
            transportationRequest.setTripType(request.tripType);
        }
        if (request.isShortNotice != null) {
            transportationRequest.setShortNotice(request.isShortNotice);
        }
        if (request.detail != null) {
            transportationRequest.setDetail(request.detail);
        }
        if (request.isAssetPickup != null) {
            transportationRequest.setAssetPickup(request.isAssetPickup);
        }
        if (request.estimatedTotalDistanceKm != null) {
            transportationRequest.setEstimatedTotalDistanceKm(request.estimatedTotalDistanceKm);
        }
        if (request.estimatedTotalDurationMin != null) {
            transportationRequest.setEstimatedTotalDurationMin(request.estimatedTotalDurationMin);
        }
        if (request.transportationRequestStateId != null) {
            transportationRequest.setTransportationRequestStateId(
                request.transportationRequestStateId);
        }

        transportationRequestService.save(transportationRequest);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId, String peopleNumber, String assets,
                          String passengers, ZonedDateTime startTime, ZonedDateTime endTime,
                          ZonedDateTime requestedDate, ZonedDateTime requestedFrom,
                          ZonedDateTime requestedTo, TripTypeEnum tripType, Boolean isShortNotice,
                          String detail, Boolean isAssetPickup, Double estimatedTotalDistanceKm,
                          Double estimatedTotalDurationMin, UUID transportationRequestStateId)
    {

    }
}
