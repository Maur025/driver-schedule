package com.kernotec.driverscheduleservice.command.resource.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.dto.resource.PersonDto;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.resource.PersonDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonGetDtoCmd extends
    AbstractTransactionalRequiredCommand<PersonGetDtoCmd.Request, PersonDto>
{

    private final PersonService personService;
    private final PersonDtoFlatMapper personDtoFlatMapper;

    @Override
    protected PersonDto run(Request request) {
        Person person = personService.findByIdThrow(request.personId);
        return personDtoFlatMapper.toDto(person);
    }

    @Builder
    public record Request(@NotNull UUID personId) {

    }
}
