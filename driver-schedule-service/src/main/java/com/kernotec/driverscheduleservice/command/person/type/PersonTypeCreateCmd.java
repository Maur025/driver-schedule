package com.kernotec.driverscheduleservice.command.person.type;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.PersonType;
import com.kernotec.driverscheduleservice.jpa.service.PersonTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonTypeCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonTypeCreateCmd.Request, UUID>
{

    private final PersonTypeService personTypeService;

    @Override
    protected UUID run(Request request) {
        var personType = new PersonType();

        personType.setName(request.name);
        personType.setCode(request.code);

        personType = personTypeService.save(personType);
        return personType.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String code) {

    }
}
