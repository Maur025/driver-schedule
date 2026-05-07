package com.kernotec.driverschedule.service.rest.mapper.resource.response.person.type;

import com.kernotec.driverschedule.service.jpa.entity.resource.PersonType;
import com.kernotec.driverschedule.service.rest.dto.resource.response.person.type.PersonTypeResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper
public interface PersonTypeResponseMapper {

    PersonTypeResponse toResponse(UUID id);

    PersonTypeResponse toResponse(PersonType personType);

    List<PersonTypeResponse> toResponse(List<PersonType> personTypeList);

    Set<PersonTypeResponse> toResponse(Set<PersonType> personTypeSet);
}
