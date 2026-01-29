package com.kernotec.driverscheduleservice.rest.mapper.label.type;

import com.kernotec.driverscheduleservice.jpa.entity.LabelType;
import com.kernotec.driverscheduleservice.rest.dto.response.LabelTypeResponse;
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
