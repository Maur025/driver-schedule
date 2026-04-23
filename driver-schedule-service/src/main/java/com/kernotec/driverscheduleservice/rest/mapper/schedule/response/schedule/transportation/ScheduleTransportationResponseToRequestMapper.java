package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.ScheduleTransportationResponse;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseUtil.class})
public interface ScheduleTransportationResponseToRequestMapper {

    @Mapping(target = "transportationRequest", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    @Mapping(target = "personRequested", ignore = true)
    @Mapping(target = "tripAssignments", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
