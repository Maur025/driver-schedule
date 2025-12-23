package com.kernotec.driverscheduleservice.rest.mapper.transportation.request;

import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class})
public interface TransportationRequestResponseFlatMapper {

    @Mapping(target = "startingCoordinates", source = "startingCoordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "endCoordinates", source = "endCoordinate",
             qualifiedByName = "mapToPositionGeoJson")
    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    TransportationRequestResponse toResponse(UUID id);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
