package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.TripState;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripStateResponse;
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
