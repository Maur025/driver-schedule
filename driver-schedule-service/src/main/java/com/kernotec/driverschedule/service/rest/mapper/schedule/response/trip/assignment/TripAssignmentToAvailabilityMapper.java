package com.kernotec.driverschedule.service.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverschedule.service.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverschedule.service.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseFlatMapper;
import com.kernotec.driverschedule.service.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import com.kernotec.driverschedule.service.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverschedule.service.rest.mapper.trip.response.trip.TripResponseFlatMapper;
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
