package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.Reason;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ReasonResponseMapper {

    ReasonResponse toResponse(Reason reason);

    ReasonResponse toResponse(UUID id);

    List<ReasonResponse> toResponse(List<Reason> reasonList);

    Set<ReasonResponse> toResponse(Set<Reason> reasonSet);
}
