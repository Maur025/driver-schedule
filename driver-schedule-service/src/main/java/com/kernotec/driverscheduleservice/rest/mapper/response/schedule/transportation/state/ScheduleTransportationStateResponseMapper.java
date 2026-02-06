package com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.state;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportationState;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.state.ScheduleTransportationStateResponse;
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
