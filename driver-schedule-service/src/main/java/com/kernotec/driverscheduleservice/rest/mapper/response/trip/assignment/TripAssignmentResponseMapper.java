package com.kernotec.driverscheduleservice.rest.mapper.response.trip.assignment;

import com.kernotec.driverscheduleservice.jpa.entity.schedule.TripAssignment;
import com.kernotec.driverscheduleservice.rest.dto.schedule.response.trip.assignment.TripAssignmentResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.person.PersonResponseWithContactMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.schedule.transportation.ScheduleTransportationResponseToAssignMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.trip.TripResponseToAssignMapper;
import com.kernotec.driverscheduleservice.rest.mapper.response.vehicle.VehicleResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseWithContactMapper.class,
    ScheduleTransportationResponseToAssignMapper.class, TripResponseToAssignMapper.class})
public interface TripAssignmentResponseMapper {

    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    TripAssignmentResponse toResponse(UUID id);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
