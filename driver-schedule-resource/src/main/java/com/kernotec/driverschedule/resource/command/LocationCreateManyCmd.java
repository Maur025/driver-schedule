package com.kernotec.driverschedule.resource.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.resource.jpa.service.LocationService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationCreateManyCmd extends
    AbstractTransactionalRequiredCommand<LocationCreateManyCmd.Request, List<Location>>
{

    private final LocationService locationService;

    @Override
    protected List<Location> run(Request request) {
        if (request.locationList()
            .isEmpty())
        {
            log.debug("No location data to create.");
            return null;
        }

        return locationService.saveAll(request.locationList());
    }

    @Builder
    public record Request(@NotNull List<Location> locationList) {

    }
}