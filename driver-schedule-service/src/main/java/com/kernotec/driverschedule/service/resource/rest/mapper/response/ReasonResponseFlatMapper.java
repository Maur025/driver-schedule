package com.kernotec.driverschedule.service.resource.rest.mapper.response;

import com.kernotec.driverschedule.service.resource.jpa.entity.Reason;
import com.kernotec.driverschedule.service.resource.rest.dto.response.ReasonResponse;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ReasonResponseFlatMapper {

    @Mapping(target = "reasonType", ignore = true)
    ReasonResponse toResponse(Reason reason);

    List<ReasonResponse> toResponse(List<Reason> reasonList);

    Set<ReasonResponse> toResponse(Set<Reason> reasonSet);
}
