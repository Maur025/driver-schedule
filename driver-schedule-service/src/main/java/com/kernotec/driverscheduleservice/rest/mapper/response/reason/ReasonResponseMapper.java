package com.kernotec.driverscheduleservice.rest.mapper.response.reason;

import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.ReasonResponse;
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
