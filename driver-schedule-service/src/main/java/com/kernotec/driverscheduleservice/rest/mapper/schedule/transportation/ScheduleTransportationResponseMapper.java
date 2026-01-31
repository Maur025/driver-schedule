package com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.transportation.request.TransportationRequestResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {PersonResponseWithContactMapper.class,
    TransportationRequestResponseFlatMapper.class, AuthUserDataResponseMapper.class})
public interface ScheduleTransportationResponseMapper {

    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
