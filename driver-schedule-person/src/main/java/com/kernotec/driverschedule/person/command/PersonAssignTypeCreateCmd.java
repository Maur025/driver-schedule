package com.kernotec.driverschedule.person.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.PersonAssignType;
import com.kernotec.driverschedule.person.jpa.service.PersonAssignTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonAssignTypeCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonAssignTypeCreateCmd.Request, UUID>
{

    private final PersonAssignTypeService personAssignTypeService;

    @Override
    protected UUID run(Request request) {
        var personAssignType = new PersonAssignType();

        personAssignType.setPersonId(request.personId);
        personAssignType.setPersonTypeId(request.personTypeId);

        personAssignType = personAssignTypeService.save(personAssignType);
        return personAssignType.getId();
    }

    @Builder
    public record Request(@NotNull UUID personId, @NotNull UUID personTypeId) {

    }
}
