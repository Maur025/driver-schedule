package com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request;

import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, DateResponseUtil.class})
public interface TransportationRequestResponseFlatMapper {

    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "scheduleTransportations", ignore = true)
    @Mapping(target = "requestCoords", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
