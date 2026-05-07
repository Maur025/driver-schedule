package com.kernotec.driverschedule.service.rest.mapper.resource.response.label.type;

import com.kernotec.driverschedule.service.jpa.entity.resource.LabelType;
import com.kernotec.driverschedule.service.rest.dto.resource.response.label.type.LabelTypeResponse;
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
