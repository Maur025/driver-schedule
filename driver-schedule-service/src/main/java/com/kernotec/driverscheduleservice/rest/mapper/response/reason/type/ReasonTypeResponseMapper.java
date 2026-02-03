package com.kernotec.driverscheduleservice.rest.mapper.response.reason.type;

import com.kernotec.driverscheduleservice.jpa.entity.ReasonType;
import com.kernotec.driverscheduleservice.rest.dto.response.reason.type.ReasonTypeResponse;
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
