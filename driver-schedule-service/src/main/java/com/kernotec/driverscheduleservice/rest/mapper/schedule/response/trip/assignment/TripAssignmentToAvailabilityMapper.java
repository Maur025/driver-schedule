package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import com.kernotec.driverscheduleservice.rest.mapper.schedule.response.schedule.transportation.ScheduleTransportationToAvailabilityMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseFlatMapper;
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
