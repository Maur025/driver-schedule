package com.kernotec.driverschedule.service.schedule.rest.mapper.response;

import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.ScheduleTransportationStateResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ScheduleTransportationStateResponseMapper {

    ScheduleTransportationStateResponse toResponse(UUID id);

    ScheduleTransportationStateResponse toResponse(
        ScheduleTransportationState scheduleTransportationState);

    List<ScheduleTransportationStateResponse> toResponse(
        List<ScheduleTransportationState> scheduleTransportationStateList);

    Set<ScheduleTransportationStateResponse> toResponse(
        Set<ScheduleTransportationState> scheduleTransportationStateSet);
}
