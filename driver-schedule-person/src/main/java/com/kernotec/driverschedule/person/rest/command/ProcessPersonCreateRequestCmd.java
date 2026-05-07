package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.properties.AuthConfigProperties;
import com.kernotec.driverschedule.person.jpa.service.ContactProcessCommon;
import com.kernotec.driverschedule.person.jpa.service.PersonTypeService;
import com.kernotec.driverschedule.person.jpa.service.UserWebFluxService;
import com.kernotec.driverschedule.person.rest.dto.request.PersonCreateRequest;
import com.kernotec.driverschedule.person.socket.PersonSocketHandler;
import com.kernotec.driverschedule.person.socket.PersonSocketTopic;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverschedule.user.api.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessPersonCreateRequestCmd extends
    AbstractCommand<ProcessPersonCreateRequestCmd.Request, UUID>
{

    private final AuthConfigProperties authConfigProperties;

    private final PersonTypeService personTypeService;

    private final PersonCreateWithTypeCmd personCreateWithTypeCmd;
    private final PersonValidationCmd personValidationCmd;
    private final ContactProcessCommon contactProcessCommon;
    private final PersonSocketHandler personSocketHandler;
    private final UserWebFluxService userWebFluxService;

    @Override
    protected void validate(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;

        personValidationCmd.withRequest(PersonValidationCmd.Request.builder()
                .personTypeIdSet(personCreateRequest.getPersonTypeIds())
                .document(personCreateRequest.getDocument())
                .build())
            .execute();
    }

    @Override
    protected UUID run(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;

        Set<String> personTypeCodes = personTypeService.getCodesOfPersonTypeIds(
            personCreateRequest.getPersonTypeIds());

        UserCreateResponse userCreateResponse = userWebFluxService.saveUserFromPerson(
            UserCreateRequest.builder()
                .name(personCreateRequest.getName())
                .lastName(personCreateRequest.getLastName())
                .username(personCreateRequest.getUsername())
                .password(personCreateRequest.getDocument())
                .realmName(authConfigProperties.getRealm())
                .resource(authConfigProperties.getResource())
                .roles(personTypeCodes)
                .build());

        UUID personId = personCreateWithTypeCmd.withRequest(
                PersonCreateWithTypeCmd.Request.builder()
                    .personCreateRequest(personCreateRequest)
                    .userCreateResponse(userCreateResponse)
                    .build())
            .execute();

        contactProcessCommon.registerManyContactsForPerson(
            personCreateRequest.getContacts(), personId);

        personSocketHandler.emitMessage(PersonSocketHandler.Request.builder()
            .personId(personId)
            .topic(PersonSocketTopic.PERSON_CREATED)
            .build());

        return personId;
    }

    @Builder
    public record Request(@NotNull @Valid PersonCreateRequest personCreateRequest) {

    }
}
