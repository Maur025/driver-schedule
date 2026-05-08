package com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.state;

import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportationState;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.state.ScheduleTransportationStateResponse;
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
