package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip;

import com.kernotec.driverscheduleservice.jpa.entity.trip.Trip;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.TripResponse;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {DateResponseUtil.class})
public interface TripResponseToAssignMapper {

    @Mapping(target = "tripAssignment", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    TripResponse toResponse(Trip trip);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
