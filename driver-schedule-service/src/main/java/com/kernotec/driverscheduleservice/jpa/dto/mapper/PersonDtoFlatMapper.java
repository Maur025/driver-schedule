package com.kernotec.driverscheduleservice.jpa.dto.mapper;

import com.kernotec.driverscheduleservice.jpa.dto.PersonDto;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
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
