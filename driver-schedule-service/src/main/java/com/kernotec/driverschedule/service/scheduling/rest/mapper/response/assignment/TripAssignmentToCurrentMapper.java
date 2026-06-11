package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.assignment;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.schedule.TripAssignmentResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.schedule.ScheduleTransportationToCurrentMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ScheduleTransportationToCurrentMapper.class})
public interface TripAssignmentToCurrentMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "estimatedStartTime", ignore = true)
    @Mapping(target = "estimatedEndTime", ignore = true)
    @Mapping(target = "vehicleId", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "driverId", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "scheduleTransportationId", ignore = true)
    @Mapping(target = "tripAssignmentStateId", ignore = true)
    @Mapping(target = "tripAssignmentState", ignore = true)
    @Mapping(target = "trips", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
