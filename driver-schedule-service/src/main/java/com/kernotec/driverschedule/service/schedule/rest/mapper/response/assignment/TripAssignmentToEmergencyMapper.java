package com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment;

import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.TripAssignmentResponse;
import com.kernotec.driverschedule.service.resource.rest.mapper.response.VehicleResponseFlatMapper;
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
