package com.kernotec.driverscheduleservice.rest.mapper.response.trip;

import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.TripResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripResponseFlatMapper {

    @Mapping(target = "tripAssignment", ignore = true)
    @Mapping(target = "tripState", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    TripResponse toResponse(Trip trip);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
