package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripState;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripStateResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface TripStateResponseMapper {

    TripStateResponse toResponse(
        com.kernotec.driverschedule.service.scheduling.jpa.entity.TripState tripState);

    TripStateResponse toResponse(UUID id);

    List<TripStateResponse> toResponse(List<TripState> tripStateList);

    Set<TripStateResponse> toResponse(Set<TripState> tripStateSet);
}
