package com.kernotec.driverscheduleservice.rest.mapper.reason;

import com.kernotec.driverscheduleservice.jpa.entity.Reason;
import com.kernotec.driverscheduleservice.rest.dto.response.ReasonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface ReasonResponseMapper {

    ReasonResponse toResponse(UUID id);

    ReasonResponse toResponse(Reason reason);

    List<ReasonResponse> toResponse(List<Reason> reasonList);

    Set<ReasonResponse> toResponse(Set<Reason> reasonSet);
}
