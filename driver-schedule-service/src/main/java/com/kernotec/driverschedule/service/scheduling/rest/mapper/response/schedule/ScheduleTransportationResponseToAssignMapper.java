package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.schedule;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.TransportationReqToScheduleResponseMapper;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    uses = {PersonResponseWithContactMapper.class, TransportationReqToScheduleResponseMapper.class,
        com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface ScheduleTransportationResponseToAssignMapper {

    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    @Mapping(target = "tripAssignments", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
