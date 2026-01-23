package com.kernotec.driverscheduleservice.rest.command.person.assign.type;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.jpa.entity.PersonAssignType;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonAssignTypeGetManyRequestCmd extends
    AbstractCommand<PersonAssignTypeGetManyRequestCmd.Request, List<PersonAssignType>>
{

    @Override
    protected List<PersonAssignType> run(Request request) {
        if (request.personTypeIdSet.isEmpty()) {
            log.debug("Person type ID set is empty, returning empty list");
            return List.of();
        }

        List<PersonAssignType> personAssignTypeList = new ArrayList<>();

        for (UUID personTypeId : request.personTypeIdSet) {
            var personAssignType = new PersonAssignType();

            personAssignType.setPersonId(request.personId);
            personAssignType.setPersonTypeId(personTypeId);

            personAssignTypeList.add(personAssignType);
        }

        return personAssignTypeList;
    }

    @Builder
    public record Request(@NotNull Set<UUID> personTypeIdSet, @NotNull UUID personId) {

    }
}
