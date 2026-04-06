package com.kernotec.driverscheduleservice.rest.mapper.schedule.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseToAssignMapper;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.vehicle.VehicleResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseWithContactMapper.class,
    TripResponseToAssignMapper.class})
public interface TripAssignmentToScheduleResponseMapper {

    @Mapping(target = "scheduleTransportation", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
