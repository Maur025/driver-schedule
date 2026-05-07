package com.kernotec.driverschedule.service.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.TripResponseToAssignMapper;
import com.kernotec.driverschedule.service.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseWithContactMapper.class,
    TripResponseToAssignMapper.class, DateResponseUtil.class})
public interface TripAssignmentToScheduleResponseMapper {

    @Mapping(target = "scheduleTransportation", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
