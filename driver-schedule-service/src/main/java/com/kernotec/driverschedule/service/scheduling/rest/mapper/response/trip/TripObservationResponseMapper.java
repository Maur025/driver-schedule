package com.kernotec.driverschedule.service.scheduling.rest.mapper.response.trip;

import com.kernotec.driverschedule.service.scheduling.jpa.entity.TripObservation;
import com.kernotec.driverschedule.service.scheduling.rest.dto.response.trip.TripObservationResponse;
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
