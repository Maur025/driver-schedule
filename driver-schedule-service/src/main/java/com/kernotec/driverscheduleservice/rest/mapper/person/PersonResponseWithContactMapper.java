package com.kernotec.driverscheduleservice.rest.mapper.person;

import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.rest.dto.response.PersonResponse;
import com.kernotec.driverscheduleservice.rest.mapper.contact.ContactResponseFlatMapper;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ContactResponseFlatMapper.class})
public interface PersonResponseWithContactMapper {

    @Mapping(target = "personTypes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdByUser", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedByUser", ignore = true)
    PersonResponse toResponse(Person person);

    PersonResponse toResponse(UUID id);

    List<PersonResponse> toResponse(List<Person> personList);

    Set<PersonResponse> toResponse(Set<Person> personSet);
}
