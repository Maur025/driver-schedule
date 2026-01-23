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
public class PersonCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonCreateCmd.Request, UUID>
{

    private final PersonService personService;

    @Override
    protected UUID run(Request request) {
        var person = new Person();

        person.setName(request.name);
        person.setLastName(request.lastName);
        person.setDocument(request.document);
        person.setUserId(request.userId);

        person = personService.save(person);
        return person.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String lastName, @NotNull String document,
                          @NotNull UUID userId)
    {

    }
}
