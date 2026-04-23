package com.kernotec.driverscheduleservice.rest.mapper.resource.response.observation;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Observation;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.ObservationResponse;
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
