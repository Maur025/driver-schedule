package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.state;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripState;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.state.TripStateResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripStateResponseMapper {

    TripStateResponse toResponse(TripState tripState);

    TripStateResponse toResponse(UUID id);

    List<TripStateResponse> toResponse(List<TripState> tripStateList);

    Set<TripStateResponse> toResponse(Set<TripState> tripStateSet);
}
