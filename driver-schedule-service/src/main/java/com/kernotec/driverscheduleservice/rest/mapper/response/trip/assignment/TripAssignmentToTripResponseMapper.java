package com.kernotec.driverscheduleservice.rest.mapper.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseToAssignMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.vehicle.VehicleResponseFlatMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseWithContactMapper.class,
    ScheduleTransportationResponseToAssignMapper.class})
public interface TripAssignmentToTripResponseMapper {

    @Mapping(target = "trips", ignore = true)
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
