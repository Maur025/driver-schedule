package com.kernotec.driverschedule.service.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {com.kernotec.driverschedule.common.mapping.DateResponseMapper.class, VehicleResponseFlatMapper.class})
public interface TripAssignmentToEmergencyMapper {


    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "scheduleTransportation", ignore = true)
    @Mapping(target = "trips", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
