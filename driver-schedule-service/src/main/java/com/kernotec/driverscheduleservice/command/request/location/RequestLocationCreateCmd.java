package com.kernotec.driverscheduleservice.command.request.location;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.RequestLocation;
import com.kernotec.driverscheduleservice.jpa.enums.LocationTypeEnum;
import com.kernotec.driverscheduleservice.jpa.service.RequestLocationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RequestLocationCreateCmd extends
    AbstractTransactionalRequiredCommand<RequestLocationCreateCmd.Request, UUID>
{

    private final RequestLocationService requestLocationService;

    @Override
    protected UUID run(Request request) {
        var requestLocation = new RequestLocation();

        requestLocation.setLocationId(request.locationId);
        requestLocation.setTransportationRequestId(request.transportationRequestId);
        requestLocation.setLocationType(request.locationType);

        requestLocation = requestLocationService.save(requestLocation);
        return requestLocation.getId();
    }

    @Builder
    public record Request(@NotNull UUID locationId, @NotNull UUID transportationRequestId,
                          @NotNull LocationTypeEnum locationType)
    {

    }
}
