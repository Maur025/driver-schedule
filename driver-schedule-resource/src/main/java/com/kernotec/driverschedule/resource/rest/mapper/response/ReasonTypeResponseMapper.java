package com.kernotec.driverschedule.resource.rest.mapper.response;

import com.kernotec.driverschedule.resource.jpa.entity.ReasonType;
import com.kernotec.driverschedule.resource.rest.dto.response.ReasonTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ReasonTypeResponseMapper {

    ReasonTypeResponse toResponse(ReasonType reasonType);

    ReasonTypeResponse toResponse(UUID id);

    List<ReasonTypeResponse> toResponse(List<ReasonType> reasonTypeList);

    Set<ReasonTypeResponse> toResponse(Set<ReasonType> reasonTypeSet);
}
