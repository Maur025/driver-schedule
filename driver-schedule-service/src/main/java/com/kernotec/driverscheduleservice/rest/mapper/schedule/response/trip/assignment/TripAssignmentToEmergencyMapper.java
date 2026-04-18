package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseUtil.class, VehicleResponseFlatMapper.class})
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
