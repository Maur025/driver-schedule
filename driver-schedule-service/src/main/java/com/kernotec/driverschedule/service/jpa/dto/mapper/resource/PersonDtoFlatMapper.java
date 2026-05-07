package com.kernotec.driverschedule.service.jpa.dto.mapper.resource;

import com.kernotec.driverschedule.service.jpa.dto.resource.PersonDto;
import com.kernotec.driverschedule.service.jpa.entity.resource.Person;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PersonDtoFlatMapper {

    @Mapping(target = "personTypes", ignore = true)
    PersonDto toDto(Person person);

    List<PersonDto> toDto(List<Person> personList);

    Set<PersonDto> toDto(Set<Person> personSet);
}
