package com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment;

import com.kernotec.driverschedule.service.schedule.jpa.entity.TripAssignment;
import com.kernotec.driverschedule.service.schedule.rest.dto.response.TripAssignmentResponse;
import com.kernotec.driverschedule.person.rest.mapper.response.PersonResponseWithContactMapper;
import com.kernotec.driverschedule.resource.rest.mapper.response.VehicleResponseFlatMapper;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.ScheduleTransportationResponseToAssignMapper;
import com.kernotec.driverschedule.service.trip.rest.mapper.response.TripResponseToAssignMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {VehicleResponseFlatMapper.class, PersonResponseWithContactMapper.class,
    ScheduleTransportationResponseToAssignMapper.class, TripResponseToAssignMapper.class,
    com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TripAssignmentResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripAssignmentResponse toResponse(TripAssignment tripAssignment);

    TripAssignmentResponse toResponse(UUID id);

    List<TripAssignmentResponse> toResponse(List<TripAssignment> tripAssignmentList);

    Set<TripAssignmentResponse> toResponse(Set<TripAssignment> tripAssignmentSet);
}
