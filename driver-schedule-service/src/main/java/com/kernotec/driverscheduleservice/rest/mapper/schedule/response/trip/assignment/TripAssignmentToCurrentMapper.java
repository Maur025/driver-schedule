package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationToCurrentMapper;
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
