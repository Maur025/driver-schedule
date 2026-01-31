package com.kernotec.driverscheduleservice.rest.mapper.person.type;

import com.kernotec.driverscheduleservice.jpa.entity.PersonType;
import com.kernotec.driverscheduleservice.rest.dto.response.person.type.PersonTypeResponse;
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
