package com.kernotec.driverscheduleservice.rest.mapper.resource.response.observation.type;

import com.kernotec.driverscheduleservice.jpa.entity.resource.ObservationType;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.observation.type.ObservationTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ObservationTypeResponseMapper {

    ObservationTypeResponse toResponse(ObservationType observationType);

    ObservationTypeResponse toResponse(UUID id);

    List<ObservationTypeResponse> toResponse(List<ObservationType> observationTypeList);

    Set<ObservationTypeResponse> toResponse(Set<ObservationType> observationTypeSet);
}
