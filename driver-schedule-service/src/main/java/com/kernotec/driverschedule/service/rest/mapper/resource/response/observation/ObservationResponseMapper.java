package com.kernotec.driverschedule.service.rest.mapper.resource.response.observation;

import com.kernotec.driverschedule.service.jpa.entity.resource.Observation;
import com.kernotec.driverschedule.service.rest.dto.resource.response.observation.ObservationResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ObservationResponseMapper {

    ObservationResponse toResponse(Observation observation);

    ObservationResponse toResponse(UUID id);

    List<ObservationResponse> toResponse(List<Observation> observationList);

    Set<ObservationResponse> toResponse(Set<Observation> observationSet);
}
