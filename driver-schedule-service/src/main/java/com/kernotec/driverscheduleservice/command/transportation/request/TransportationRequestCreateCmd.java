package com.kernotec.driverscheduleservice.command.transportation.request;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.jpa.enums.TripTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.TransportationRequestService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
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

    @Override
    protected UUID run(Request request) {
        var transportationRequest = new TransportationRequest();

        transportationRequest.setStartingCoordinate(request.startingCoordinate);
        transportationRequest.setEndCoordinate(request.endCoordinate);
        transportationRequest.setPeopleNumber(request.peopleNumber);
        transportationRequest.setAssets(request.assets);
        transportationRequest.setPassengers(request.passengers);
        transportationRequest.setStartTime(request.startTime);
        transportationRequest.setEndTime(request.endTime);
        transportationRequest.setRequestedDate(request.requestedDate);
        transportationRequest.setTripType(request.tripType);
        transportationRequest.setTransportationRequestStateId(request.transportationRequestStateId);

        transportationRequest = transportationRequestService.save(transportationRequest);
        return transportationRequest.getId();
    }

    @Builder
    public record Request(@NotNull Coordinate startingCoordinate, @NotNull Coordinate endCoordinate,
                          @NotNull String peopleNumber, @NotNull ZonedDateTime startTime,
                          @NotNull ZonedDateTime endTime, @NotNull ZonedDateTime requestedDate,
                          @NotNull TripTypeEnum tripType,
                          @NotNull UUID transportationRequestStateId, String passengers,
                          String assets)
    {

    }
}
