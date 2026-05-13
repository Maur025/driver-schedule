package com.kernotec.driverschedule.resource.rest.mapper.response;

import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import com.kernotec.driverschedule.resource.jpa.entity.Location;
import com.kernotec.driverschedule.resource.rest.dto.response.LocationResponse;
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
