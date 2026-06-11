package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.Trip;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TripNAResponseFlatMapper {

    @Mapping(target = "tripAssignment", ignore = true)
    @Mapping(target = "tripState", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    TripResponse toResponse(Trip trip);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
