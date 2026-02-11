package com.kernotec.driverscheduleservice.rest.mapper.response.request.coord;

import com.kernotec.driverscheduleservice.jpa.entity.RequestCoord;
import com.kernotec.driverscheduleservice.rest.dto.response.request.coord.RequestCoordResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.location.LocationResponseFlatMapper;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, LocationResponseFlatMapper.class})
public interface RequestCoordToRequestResponseMapper {

    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    @Mapping(target = "transportationRequest", ignore = true)
    RequestCoordResponse toResponse(RequestCoord requestCoord);

    RequestCoordResponse toResponse(UUID id);

    List<RequestCoordResponse> toResponse(List<RequestCoord> requestCoordList);

    Set<RequestCoordResponse> toResponse(Set<RequestCoord> requestCoordSet);
}
