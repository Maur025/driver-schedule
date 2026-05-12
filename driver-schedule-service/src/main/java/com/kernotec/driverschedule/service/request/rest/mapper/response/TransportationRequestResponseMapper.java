package com.kernotec.driverschedule.service.request.rest.mapper.response;

import com.kernotec.driverschedule.common.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverschedule.service.request.jpa.entity.TransportationRequest;
import com.kernotec.driverschedule.service.request.rest.dto.response.TransportationRequestResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationResponseToRequestMapper;
import com.kernotec.driverschedule.common.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {GeoJsonUtil.class, ScheduleTransportationResponseToRequestMapper.class,
    PersonResponseWithContactMapper.class, AuthUserDataResponseMapper.class,
    RequestCoordToRequestResponseMapper.class, RejectReasonWithReasonResponseMapper.class,
    CancelReqReasonWithReasonResponseMapper.class, com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
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
