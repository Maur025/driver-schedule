package com.kernotec.driverschedule.service.rest.mapper.resource.response.location;

import com.kernotec.driverschedule.service.jpa.entity.resource.Location;
import com.kernotec.driverschedule.service.rest.dto.resource.response.location.LocationResponse;
import com.kernotec.driverschedule.service.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class})
public interface LocationResponseFlatMapper {

    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    @Mapping(target = "placeCategory", ignore = true)
    LocationResponse toResponse(Location location);

    List<LocationResponse> toResponse(List<Location> locationList);

    Set<LocationResponse> toResponse(Set<Location> locationSet);

}
