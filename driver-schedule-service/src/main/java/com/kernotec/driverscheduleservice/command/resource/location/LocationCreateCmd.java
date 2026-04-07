package com.kernotec.driverscheduleservice.command.resource.location;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Location;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
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
        location.setIcon(request.icon);
        location.setColor(request.color);
        location.setPlaceCategoryId(request.placeCategoryId);

        location = locationService.save(location);
        return location.getId();
    }

    @Builder
    public record Request(@NotNull Coordinate coordinate, @NotNull String name, String description,
                          String icon, String color, @NotNull UUID placeCategoryId)
    {

    }
}
