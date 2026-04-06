package com.kernotec.driverscheduleservice.rest.mapper.response.label.type;

import com.kernotec.driverscheduleservice.jpa.entity.resource.LabelType;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.label.type.LabelTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface LabelTypeResponseMapper {

    LabelTypeResponse toResponse(LabelType labelType);

    LabelTypeResponse toResponse(UUID id);

    List<LabelTypeResponse> toResponse(List<LabelType> labelTypeList);

    Set<LabelTypeResponse> toResponse(Set<LabelType> labelTypeSet);
}
