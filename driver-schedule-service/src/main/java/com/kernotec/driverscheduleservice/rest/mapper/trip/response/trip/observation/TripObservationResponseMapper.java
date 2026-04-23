package com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.observation;

import com.kernotec.driverscheduleservice.jpa.entity.trip.TripObservation;
import com.kernotec.driverscheduleservice.rest.dto.trip.response.trip.observation.TripObservationResponse;
import com.kernotec.driverscheduleservice.rest.mapper.trip.response.trip.TripResponseFlatMapper;
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
