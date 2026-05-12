package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.Location;
import com.kernotec.driverschedule.service.resource.rest.dto.response.LocationResponse;
import com.kernotec.driverschedule.service.common.util.GeoJsonUtil;
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
