package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.Observation;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ObservationResponse;
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
