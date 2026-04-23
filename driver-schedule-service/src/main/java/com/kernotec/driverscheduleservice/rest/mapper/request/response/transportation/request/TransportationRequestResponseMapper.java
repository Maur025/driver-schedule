package com.kernotec.driverscheduleservice.rest.mapper.request.response.transportation.request;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.request.TransportationRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.cancel.request.reason.CancelReqReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.reject.reason.RejectReasonWithReasonResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.request.response.request.coord.RequestCoordToRequestResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseToRequestMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
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
