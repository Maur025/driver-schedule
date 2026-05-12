package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.Trip;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {com.kernotec.driverschedule.common.mapping.DateResponseMapper.class})
public interface TripResponseFlatMapper {

    @Mapping(target = "tripAssignment", ignore = true)
    @Mapping(target = "tripState", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripResponse toResponse(Trip trip);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
