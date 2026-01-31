package com.kernotec.driverscheduleservice.rest.mapper.schedule.transportation.log;

import com.kernotec.driverscheduleservice.jpa.entity.ScheduleTransportationLog;
import com.kernotec.driverscheduleservice.rest.dto.response.schedule.transportation.log.ScheduleTransportationLogResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ScheduleTransportationLogResponseMapper {

    ScheduleTransportationLogResponse toResponse(
        ScheduleTransportationLog scheduleTransportationLog);

    ScheduleTransportationLogResponse toResponse(UUID id);

    List<ScheduleTransportationLogResponse> toResponse(
        List<ScheduleTransportationLog> scheduleTransportationLogList);

    Set<ScheduleTransportationLogResponse> toResponse(
        Set<ScheduleTransportationLog> scheduleTransportationLogSet);
}
