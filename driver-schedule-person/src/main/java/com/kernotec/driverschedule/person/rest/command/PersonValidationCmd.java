package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.Person;
import com.kernotec.driverschedule.person.exception.PersonException;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonValidationCmd extends
    AbstractTransactionalRequiredCommand<PersonValidationCmd.Request, Void>
{

    private final PersonService personService;

    @Override
    protected Void run(Request request) {
        if (request.personTypeIdSet != null && request.personTypeIdSet.isEmpty()) {
            throw new PersonException("type.list.empty", "", HttpStatus.BAD_REQUEST.value());
        }

        if (request.document != null) {
            documentValidation(request.document, request.personId);
        }

        return null;
    }

    private void documentValidation(String document, UUID personId) {
        Optional<Person> personOptional = personService.findByDocument(document, personId);

        if (personOptional.isPresent()) {
            throw new PersonException(
                "document.already.exists", "'" + document + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Builder
    public record Request(String document, Set<UUID> personTypeIdSet, UUID personId) {

    }
}
