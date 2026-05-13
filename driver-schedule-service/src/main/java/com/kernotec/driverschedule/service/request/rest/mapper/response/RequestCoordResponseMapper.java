package com.kernotec.driverschedule.service.request.rest.mapper.response;

import com.kernotec.driverschedule.service.request.jpa.entity.RequestCoord;
import com.kernotec.driverschedule.service.request.rest.dto.response.RequestCoordResponse;
import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, TransportationRequestResponseFlatMapper.class})
public interface RequestCoordResponseMapper {

    @Mapping(target = "coordinates", source = "coordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "longitude", source = "coordinate.lng")
    @Mapping(target = "latitude", source = "coordinate.lat")
    RequestCoordResponse toResponse(RequestCoord requestCoord);

    RequestCoordResponse toResponse(UUID id);

    List<RequestCoordResponse> toResponse(List<RequestCoord> requestCoordList);

    Set<RequestCoordResponse> toResponse(Set<RequestCoord> requestCoordSet);
}
