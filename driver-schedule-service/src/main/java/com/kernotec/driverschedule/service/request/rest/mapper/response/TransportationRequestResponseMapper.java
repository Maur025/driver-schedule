package com.kernotec.driverschedule.service.request.rest.mapper.response;

import com.kernotec.driverschedule.service.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseToRequestMapper;
import com.kernotec.driverschedule.service.util.DateResponseUtil;
import com.kernotec.driverschedule.service.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, ScheduleTransportationResponseToRequestMapper.class,
    PersonResponseWithContactMapper.class, AuthUserDataResponseMapper.class,
    RequestCoordToRequestResponseMapper.class, RejectReasonWithReasonResponseMapper.class,
    CancelReqReasonWithReasonResponseMapper.class, DateResponseUtil.class})
public interface TransportationRequestResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    TransportationRequestResponse toResponse(UUID id, Long correlative, String code);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
