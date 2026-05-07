package com.kernotec.driverschedule.service.rest.mapper.resource.response.reason.type;

import com.kernotec.driverschedule.service.jpa.entity.resource.ReasonType;
import com.kernotec.driverschedule.service.rest.dto.resource.response.reason.type.ReasonTypeResponse;
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
