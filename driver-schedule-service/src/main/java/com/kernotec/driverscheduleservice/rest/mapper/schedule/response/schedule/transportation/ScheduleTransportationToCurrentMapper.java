package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.request.rest.mapper.response.TransportationRequestToCurrentMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TransportationRequestToCurrentMapper.class})
public interface ScheduleTransportationToCurrentMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "scheduleFrom", ignore = true)
    @Mapping(target = "scheduleTo", ignore = true)
    @Mapping(target = "scheduledDate", ignore = true)
    @Mapping(target = "transportationRequestId", ignore = true)
    @Mapping(target = "personRequestedId", ignore = true)
    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "scheduleTransportationStateId", ignore = true)
    @Mapping(target = "scheduleTransportationState", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    @Mapping(target = "tripAssignments", ignore = true)
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
