package com.kernotec.driverschedule.person.rest.mapper.response;

import com.kernotec.driverschedule.person.jpa.entity.PersonType;
import com.kernotec.driverschedule.person.rest.dto.response.PersonTypeResponse;
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
