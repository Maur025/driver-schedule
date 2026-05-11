package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripResponse;
import com.kernotec.driverschedule.service.schedule.rest.mapper.response.assignment.TripAssignmentToCurrentMapper;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripAssignmentToCurrentMapper.class})
public interface TripCurrentResponseMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "tripStateId", ignore = true)
    @Mapping(target = "tripAssignmentId", ignore = true)
    @Mapping(target = "durationTotalMinutes", ignore = true)
    @Mapping(target = "onRouteTimeMinutes", ignore = true)
    @Mapping(target = "waitTimeMinutes", ignore = true)
    TripResponse toResponse(Trip trip);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
