package com.kernotec.driverscheduleservice.rest.command.person;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.person.PersonCreateCmd;
import com.kernotec.driverscheduleservice.command.person.assign.type.PersonAssignTypeCreateCmd;
import com.kernotec.driverscheduleservice.exception.PersonException;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import com.kernotec.driverscheduleservice.rest.dto.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserDeleteRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonCreateWithTypeCmd extends
    AbstractTransactionalRequiredCommand<PersonCreateWithTypeCmd.Request, UUID>
{

    private final PersonService personService;

    private final PersonCreateCmd personCreateCmd;
    private final PersonAssignTypeCreateCmd personAssignTypeCreateCmd;

    @Override
    protected UUID run(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;
        UserCreateResponse userCreateResponse = request.userCreateResponse;

        try {
            UUID personId = personCreateCmd.withRequest(PersonCreateCmd.Request.builder()
                    .name(personCreateRequest.getName())
                    .lastName(personCreateRequest.getLastName())
                    .document(personCreateRequest.getDocument())
                    .userId(userCreateResponse.getId())
                    .phone(personCreateRequest.getPhone())
                    .build())
                .execute();

            for (UUID personTypeId : personCreateRequest.getPersonTypeIds()) {
                personAssignTypeCreateCmd.withRequest(PersonAssignTypeCreateCmd.Request.builder()
                        .personId(personId)
                        .personTypeId(personTypeId)
                        .build())
                    .execute();
            }

            return personId;
        } catch (Exception ex) {
            log.error("Error creating person, deleting user with error: ", ex);

            personService.deleteUserFromPerson(
                userCreateResponse.getId(), UserDeleteRequest.builder()
                    .userName(personCreateRequest.getUsername())
                    .realmName("driver-schedule-auth")
                    .build()
            );

            throw new PersonException("register.failed", "", HttpStatus.CONFLICT.value());
        }
    }

    @Builder
    public record Request(@NotNull PersonCreateRequest personCreateRequest,
                          @NotNull UserCreateResponse userCreateResponse)
    {

    }
}
