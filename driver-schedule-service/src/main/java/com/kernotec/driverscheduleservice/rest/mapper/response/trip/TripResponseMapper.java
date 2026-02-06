package com.kernotec.driverscheduleservice.rest.mapper.response.trip;

import com.kernotec.driverscheduleservice.jpa.entity.Trip;
import com.kernotec.driverscheduleservice.rest.dto.response.trip.TripResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripResponseMapper {

    TripResponse toResponse(Trip trip);

    TripResponse toResponse(UUID id);

    List<TripResponse> toResponse(List<Trip> tripList);

    Set<TripResponse> toResponse(Set<Trip> tripSet);
}
