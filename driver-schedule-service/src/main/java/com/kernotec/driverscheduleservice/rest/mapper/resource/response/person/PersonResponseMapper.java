package com.kernotec.driverscheduleservice.rest.mapper.resource.response.person;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.contact.ContactResponseFlatMapper;
import com.kernotec.driverscheduleservice.util.DateResponseUtil;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ContactResponseFlatMapper.class, AuthUserDataResponseMapper.class,
    DateResponseUtil.class})
public interface PersonResponseMapper {

    @Mapping(target = "createdAt", qualifiedByName = "mapToZonedDateTimeResponse")
    @Mapping(target = "updatedAt", qualifiedByName = "mapToZonedDateTimeResponse")
    PersonResponse toResponse(Person person);

    PersonResponse toResponse(UUID id);

    List<PersonResponse> toResponse(List<Person> personList);

    Set<PersonResponse> toResponse(Set<Person> personSet);
}
