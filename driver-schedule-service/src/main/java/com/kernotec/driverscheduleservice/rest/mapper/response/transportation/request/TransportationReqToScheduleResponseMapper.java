package com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request;

import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.request.coord.RequestCoordToRequestResponseMapper;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, RequestCoordToRequestResponseMapper.class})
public interface TransportationReqToScheduleResponseMapper {

    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "scheduleTransportations", ignore = true)
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
