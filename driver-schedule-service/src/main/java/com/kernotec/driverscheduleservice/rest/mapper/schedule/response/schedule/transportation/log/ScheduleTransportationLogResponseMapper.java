package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.log;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.ScheduleTransportationLog;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.schedule.transportation.log.ScheduleTransportationLogResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationResponseFlatMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseUtil.class, ScheduleTransportationResponseFlatMapper.class})
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
