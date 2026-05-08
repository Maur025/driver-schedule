package com.kernotec.driverschedule.person.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
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
        if (request.document != null) {
            person.setDocument(request.document);
        }

        personService.save(person);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID personId, String name, String lastName, String document) {

    }
}
