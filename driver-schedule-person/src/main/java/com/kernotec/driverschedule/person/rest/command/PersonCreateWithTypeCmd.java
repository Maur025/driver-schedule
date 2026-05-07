package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.common.properties.AuthConfigProperties;
import com.kernotec.driverschedule.person.command.PersonAssignTypeManyCreateCmd;
import com.kernotec.driverschedule.person.command.PersonCreateCmd;
import com.kernotec.driverschedule.person.exception.PersonException;
import com.kernotec.driverschedule.person.jpa.entity.PersonAssignType;
import com.kernotec.driverschedule.person.jpa.service.UserWebFluxService;
import com.kernotec.driverschedule.person.rest.dto.request.PersonCreateRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserDeleteRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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

    private final AuthConfigProperties authConfigProperties;

    private final PersonCreateCmd personCreateCmd;
    private final PersonAssignTypeGetManyRequestCmd personAssignTypeGetManyRequestCmd;
    private final PersonAssignTypeManyCreateCmd personAssignTypeManyCreateCmd;
    private final UserWebFluxService userWebFluxService;

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
                    .build())
                .execute();

            List<PersonAssignType> personAssignTypeListToSave = personAssignTypeGetManyRequestCmd.withRequest(
                    PersonAssignTypeGetManyRequestCmd.Request.builder()
                        .personTypeIdSet(personCreateRequest.getPersonTypeIds())
                        .personId(personId)
                        .build())
                .execute();

            personAssignTypeManyCreateCmd.withRequest(
                    PersonAssignTypeManyCreateCmd.Request.builder()
                        .personAssignTypeList(personAssignTypeListToSave)
                        .build())
                .execute();

            return personId;
        } catch (Exception ex) {
            log.error("Error creating person, deleting user with error: ", ex);

            userWebFluxService.deleteUserFromPerson(
                userCreateResponse.getId(), UserDeleteRequest.builder()
                    .userName(personCreateRequest.getUsername())
                    .realmName(authConfigProperties.getRealm())
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
