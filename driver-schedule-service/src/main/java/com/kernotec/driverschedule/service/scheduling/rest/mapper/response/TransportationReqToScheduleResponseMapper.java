package com.kernotec.driverschedule.service.scheduling.rest.mapper.response;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.scheduling.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, RequestCoordToRequestResponseMapper.class,
    AuthUserDataResponseMapper.class, com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TransportationReqToScheduleResponseMapper {

    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "rejectReasons", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "scheduleTransportations", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
