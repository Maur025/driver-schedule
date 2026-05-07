package com.kernotec.driverschedule.service.rest.mapper.resource.response.reason;

import com.kernotec.driverschedule.service.jpa.entity.resource.Reason;
import com.kernotec.driverschedule.service.rest.dto.resource.response.reason.ReasonResponse;
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
