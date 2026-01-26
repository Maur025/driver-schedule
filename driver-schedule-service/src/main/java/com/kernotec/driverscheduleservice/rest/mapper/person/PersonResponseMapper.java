package com.kernotec.driverscheduleservice.rest.mapper.person;

import com.kernotec.driverscheduleservice.audit.user.mapper.AuthUserDataResponseMapper;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.rest.dto.response.PersonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.contact.ContactResponseFlatMapper;
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
