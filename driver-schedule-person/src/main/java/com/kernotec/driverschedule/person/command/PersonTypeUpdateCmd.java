package com.kernotec.driverschedule.person.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.PersonType;
import com.kernotec.driverschedule.person.jpa.service.PersonTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonTypeUpdateCmd extends
    AbstractTransactionalRequiredCommand<PersonTypeUpdateCmd.Request, Void>
{

    private final PersonTypeService personTypeService;

    @Override
    protected Void run(Request request) {
        PersonType personType = personTypeService.findByIdThrow(request.personTypeId);

        if (request.name != null) {
            personType.setName(request.name);
        }

        personTypeService.save(personType);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID personTypeId, String name) {

    }
}
