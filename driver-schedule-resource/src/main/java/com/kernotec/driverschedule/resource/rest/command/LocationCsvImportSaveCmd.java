package com.kernotec.driverschedule.resource.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.common.dto.Coordinate;
import com.kernotec.driverschedule.resource.command.LocationCreateManyCmd;
import com.kernotec.driverschedule.resource.exception.LocationException;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.resource.jpa.entity.PlaceCategory;
import com.kernotec.driverschedule.resource.jpa.enums.PlaceCategoryCode;
import com.kernotec.driverschedule.resource.jpa.service.PlaceCategoryService;
import com.kernotec.driverschedule.resource.rest.dto.LocationCsvImportDto;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationCsvImportSaveCmd extends
    AbstractTransactionalRequiredCommand<LocationCsvImportSaveCmd.Request, Void>
{

    private final PlaceCategoryService placeCategoryService;
    private final LocationCreateManyCmd locationCreateManyCmd;

    @Override
    protected Void run(Request request) {
        List<LocationCsvImportDto> locationCsvImportDtoList = request.locationCsvImportDtoList();

        if (locationCsvImportDtoList.isEmpty()) {
            log.debug("No location data to import.");
            return null;
        }

        Map<PlaceCategoryCode, UUID> placeCategoryMap = placeCategoryService.findAll()
            .stream()
            .collect(Collectors.toMap(
                placeCategory -> PlaceCategoryCode.fromValue(placeCategory.getCode()),
                PlaceCategory::getId, (prev, next) -> prev
            ));

        List<Location> locationToCreateList = new ArrayList<>();

        for (LocationCsvImportDto locationCsvImportDto : locationCsvImportDtoList) {
            Location location = getLocation(locationCsvImportDto, placeCategoryMap);
            locationToCreateList.add(location);
        }

        locationCreateManyCmd.withRequest(LocationCreateManyCmd.Request.builder()
                .locationList(locationToCreateList)
                .build())
            .execute();

        return null;
    }

    private Location getLocation(LocationCsvImportDto locationDto,
        Map<PlaceCategoryCode, UUID> placeCategoryMap)
    {
        Coordinate coordinate = getCoordinate(locationDto);

        var location = new Location();

        location.setCoordinate(coordinate);
        location.setName(locationDto.getName());
        location.setDescription(locationDto.getDescription());
        location.setPlaceCategoryId(placeCategoryMap.get(locationDto.getPlaceCategoryCode()));

        return location;
    }

    private Coordinate getCoordinate(LocationCsvImportDto dto) {
        if (dto.getLat() == null || dto.getLng() == null) {
            throw new LocationException("No coordinates provided");
        }

        var coordinate = new Coordinate();
        coordinate.setLat(dto.getLat());
        coordinate.setLng(dto.getLng());

        return coordinate;
    }

    @Builder
    public record Request(@NotNull List<LocationCsvImportDto> locationCsvImportDtoList) {

    }
}