package com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment;

import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.TripAssignmentResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseFlatMapper;
import com.kernotec.driverschedule.service.resource.rest.mapper.response.VehicleResponseFlatMapper;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverschedule.service.trip.rest.mapper.response.TripResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseFlatMapper.class,
    TripResponseFlatMapper.class, ScheduleTransportationToAvailabilityMapper.class})
public interface TripAssignmentToAvailabilityMapper {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
