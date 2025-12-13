package com.kernotec.driverscheduleservice.command.location;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.jpa.service.LocationService;
import com.kernotec.driverscheduleservice.jpa.util.Coordinate;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocationCreateCmd extends
    AbstractTransactionalRequiredCommand<LocationCreateCmd.Request, UUID>
{

    private final LocationService locationService;

    @Override
    protected UUID run(Request request) {
        var location = new Location();

        location.setCoordinate(request.coordinate);
        location.setName(request.name);
        location.setDescription(request.description);

        location = locationService.save(location);
        return location.getId();
    }

    @Builder
    public record Request(@NotNull Coordinate coordinate, @NotNull String name,
                          String description)
    {

    }
}
