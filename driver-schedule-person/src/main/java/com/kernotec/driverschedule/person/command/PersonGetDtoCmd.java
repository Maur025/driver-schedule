package com.kernotec.driverschedule.person.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.mapper.PersonDtoFlatMapper;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
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
