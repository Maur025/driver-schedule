package com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportation;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.ScheduleTransportationResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScheduleTransportationResponseFlatMapper {

    @Mapping(target = "transportationRequest", ignore = true)
    @Mapping(target = "scheduleTransportationState", ignore = true)
    @Mapping(target = "cancelReasons", ignore = true)
    @Mapping(target = "rescheduleReasons", ignore = true)
    ScheduleTransportationResponse toResponse(ScheduleTransportation scheduleTransportation);

    ScheduleTransportationResponse toResponse(UUID id);

    List<ScheduleTransportationResponse> toResponse(
        List<ScheduleTransportation> scheduleTransportationList);

    Set<ScheduleTransportationResponse> toResponse(
        Set<ScheduleTransportation> scheduleTransportationSet);
}
