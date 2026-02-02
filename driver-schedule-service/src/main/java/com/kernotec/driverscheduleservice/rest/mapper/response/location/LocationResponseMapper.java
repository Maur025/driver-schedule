package com.kernotec.driverscheduleservice.rest.mapper.response.location;

import com.kernotec.driverscheduleservice.jpa.entity.Location;
import com.kernotec.driverscheduleservice.rest.dto.response.location.LocationResponse;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class})
public interface LocationResponseMapper {

    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    LocationResponse toResponse(Location location);

    LocationResponse toResponse(UUID id);

    List<LocationResponse> toResponse(List<Location> locationList);

    Set<LocationResponse> toResponse(Set<Location> locationSet);
}
