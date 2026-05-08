package com.kernotec.driverschedule.person.jpa.mapper;

import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.person.jpa.entity.Person;
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
