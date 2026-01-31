package com.kernotec.driverscheduleservice.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
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
        if (request.tripType != null) {
            transportationRequest.setTripType(request.tripType);
        }
        if (request.transportationRequestStateId != null) {
            transportationRequest.setTransportationRequestStateId(
                request.transportationRequestStateId);
        }

        transportationRequestService.save(transportationRequest);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID transportationRequestId, String peopleNumber,
                          ZonedDateTime startTime, ZonedDateTime endTime, TripTypeEnum tripType,
                          UUID transportationRequestStateId, String passengers, String assets)
    {

    }
}
