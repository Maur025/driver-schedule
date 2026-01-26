package com.kernotec.driverscheduleservice.rest.mapper.person;

import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.rest.dto.response.PersonResponse;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonResponseFlatMapper {

    @Mapping(target = "personTypes", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    PersonResponse toResponse(Person person);

    PersonResponse toResponse(UUID id);

    List<PersonResponse> toResponse(List<Person> personList);

    Set<PersonResponse> toResponse(Set<Person> personSet);
}
