package com.kernotec.driverscheduleservice.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonUpdateCmd extends
    AbstractTransactionalRequiredCommand<PersonUpdateCmd.Request, Void>
{

    private final PersonService personService;

    @Override
    protected Void run(Request request) {
        Person person = personService.findByIdThrow(request.personId);

        if (request.name != null) {
            person.setName(request.name);
        }
        if (request.lastName != null) {
            person.setLastName(request.lastName);
        }
        if (request.personTypeId != null) {
            person.setPersonTypeId(request.personTypeId);
        }
        if (request.phone != null) {
            person.setPhone(request.phone);
        }

        personService.save(person);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID personId, String name, String lastName, UUID personTypeId,
                          String phone)
    {

    }
}
