package com.kernotec.driverschedule.service.trip.rest.mapper.response;

import com.kernotec.driverschedule.service.trip.jpa.entity.TripObservation;
import com.kernotec.driverschedule.service.trip.rest.dto.response.TripObservationResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {TripResponseFlatMapper.class})
public interface TripObservationResponseMapper {

    TripObservationResponse toResponse(TripObservation tripObservation);

    TripObservationResponse toResponse(UUID id);

    List<TripObservationResponse> toResponse(List<TripObservation> tripObservationList);

    Set<TripObservationResponse> toResponse(Set<TripObservation> tripObservationSet);
}
