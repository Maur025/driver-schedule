package com.kernotec.driverscheduleservice.rest.mapper.response.reason;

import com.kernotec.driverscheduleservice.jpa.entity.resource.Reason;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.reason.ReasonResponse;
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
