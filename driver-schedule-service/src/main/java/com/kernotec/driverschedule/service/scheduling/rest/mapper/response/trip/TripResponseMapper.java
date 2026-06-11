package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripResponse;
import com.kernotec.driverschedule.service.scheduling.rest.mapper.response.assignment.TripAssignmentToTripResponseMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {TripAssignmentToTripResponseMapper.class,
    com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TripResponseMapper {

    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripResponse toResponse(Trip trip);

    TripResponse toResponse(UUID id);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
