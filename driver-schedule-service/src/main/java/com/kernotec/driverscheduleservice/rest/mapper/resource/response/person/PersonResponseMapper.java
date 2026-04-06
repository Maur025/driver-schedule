package com.kernotec.driverscheduleservice.rest.mapper.resource.response.person;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.rest.dto.resource.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.contact.ContactResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(uses = {ContactResponseFlatMapper.class, AuthUserDataResponseMapper.class})
public interface PersonResponseMapper {

    PersonResponse toResponse(UUID id);

    PersonResponse toResponse(Person person);

    List<PersonResponse> toResponse(List<Person> personList);

    Set<PersonResponse> toResponse(Set<Person> personSet);
}
