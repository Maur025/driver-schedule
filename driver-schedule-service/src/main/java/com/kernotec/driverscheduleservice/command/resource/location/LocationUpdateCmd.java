package com.kernotec.driverscheduleservice.command.resource.location;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Location;
import com.kernotec.driverscheduleservice.jpa.service.resource.LocationService;
import com.kernotec.driverscheduleservice.common.dto.Coordinate;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LocationUpdateCmd extends
    AbstractTransactionalRequiredCommand<LocationUpdateCmd.Request, Void>
{

    private final LocationService locationService;

    @Override
    protected Void run(Request request) {
        Location location = locationService.findByIdThrow(request.locationId);

        if (request.coordinate != null) {
            location.setCoordinate(request.coordinate);
        }
        if (request.name != null) {
            location.setName(request.name);
        }
        if (request.description != null) {
            location.setDescription(request.description);
        }
        if (request.icon != null) {
            location.setIcon(request.icon);
        }
        if (request.color != null) {
            location.setColor(request.color);
        }
        if (request.placeCategoryId != null) {
            location.setPlaceCategoryId(request.placeCategoryId);
        }

        locationService.save(location);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID locationId, Coordinate coordinate, String name,
                          String description, String icon, String color, UUID placeCategoryId)
    {

    }
}
