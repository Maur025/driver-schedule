package com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.TransportationRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.transportation.request.TransportationRequestResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.request.coord.RequestCoordToRequestResponseMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseToRequestMapper;
import com.kernotec.driverscheduleservice.util.GeoJsonUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {GeoJsonUtil.class, ScheduleTransportationResponseToRequestMapper.class,
    PersonResponseWithContactMapper.class, AuthUserDataResponseMapper.class,
    RequestCoordToRequestResponseMapper.class})
public interface TransportationRequestResponseMapper {

    TransportationRequestResponse toResponse(TransportationRequest transportationRequest);

    TransportationRequestResponse toResponse(UUID id, Long correlative, String code,
        String voucherUrl);

    List<TransportationRequestResponse> toResponse(
        List<TransportationRequest> transportationRequestList);

    Set<TransportationRequestResponse> toResponse(
        Set<TransportationRequest> transportationRequestSet);
}
