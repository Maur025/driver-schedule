package com.kernotec.driverscheduleservice.command.resource.person.assign.type;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.PersonAssignType;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonAssignTypeService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonAssignTypeManyCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonAssignTypeManyCreateCmd.Request, List<PersonAssignType>>
{

    private final PersonAssignTypeService personAssignTypeService;

    @Override
    protected List<PersonAssignType> run(Request request) {
        if (request.personAssignTypeList.isEmpty()) {
            log.debug("Person assign type list is empty, returning empty list");
            return List.of();
        }

        return personAssignTypeService.saveAll(request.personAssignTypeList);
    }

    @Builder
    public record Request(@NotNull List<PersonAssignType> personAssignTypeList) {

    }
}
