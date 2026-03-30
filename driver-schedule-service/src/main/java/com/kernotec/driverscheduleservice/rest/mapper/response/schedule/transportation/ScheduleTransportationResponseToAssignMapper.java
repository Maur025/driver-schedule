package com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.transportation.request.TransportationReqToScheduleResponseMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {PersonResponseWithContactMapper.class, TransportationReqToScheduleResponseMapper.class})
public interface ScheduleTransportationResponseToAssignMapper {

    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    @Mapping(target = "tripAssignments", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
