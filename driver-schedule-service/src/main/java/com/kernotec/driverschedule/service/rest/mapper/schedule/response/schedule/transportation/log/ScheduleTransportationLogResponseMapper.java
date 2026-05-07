package com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.log;

import com.kernotec.driverschedule.service.jpa.entity.schedule.ScheduleTransportationLog;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.schedule.transportation.log.ScheduleTransportationLogResponse;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
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
