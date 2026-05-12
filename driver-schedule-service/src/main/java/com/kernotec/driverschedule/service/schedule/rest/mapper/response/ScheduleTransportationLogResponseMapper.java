package com.kernotec.driverschedule.service.schedule.rest.mapper.response;

import com.kernotec.driverschedule.service.schedule.jpa.entity.ScheduleTransportationLog;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.ScheduleTransportationLogResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {com.kernotec.driverschedule.common.mapping.DateResponseMapper.class, ScheduleTransportationResponseFlatMapper.class})
public interface ScheduleTransportationLogResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    ScheduleTransportationLogResponse toResponse(
        ScheduleTransportationLog scheduleTransportationLog);

    ScheduleTransportationLogResponse toResponse(UUID id);

    List<ScheduleTransportationLogResponse> toResponse(
        List<ScheduleTransportationLog> scheduleTransportationLogList);

    Set<ScheduleTransportationLogResponse> toResponse(
        Set<ScheduleTransportationLog> scheduleTransportationLogSet);
}
