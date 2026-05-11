package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.ObservationType;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ObservationTypeResponse;
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
