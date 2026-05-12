package com.kernotec.driverschedule.service.schedule.rest.mapper.response;

import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportation;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.ScheduleTransportationResponse;
import com.kernotec.driverschedule.service.request.rest.mapper.response.TransportationRequestResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TransportationRequestResponseFlatMapper.class})
public interface ScheduleTransportationToAvailabilityMapper {

    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    @Mapping(target = "tripAssignments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
