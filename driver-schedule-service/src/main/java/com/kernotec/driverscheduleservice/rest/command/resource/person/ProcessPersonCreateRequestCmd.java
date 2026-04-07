package com.kernotec.driverscheduleservice.rest.command.resource.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.common.ContactProcessCommon;
import com.kernotec.driverscheduleservice.config.AuthConfigProperties;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Person;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonTypeService;
import com.kernotec.driverscheduleservice.rest.dto.resource.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.common.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.resource.response.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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

    private final PersonService personService;
    private final PersonTypeService personTypeService;

    private final PersonResponseMapper personResponseMapper;

    private final PersonCreateWithTypeCmd personCreateWithTypeCmd;
    private final PersonValidationCmd personValidationCmd;
    private final WebSocketHandler webSocketHandler;
    private final ContactProcessCommon contactProcessCommon;

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

        UserCreateResponse userCreateResponse = personService.saveUserFromPerson(
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

        Person person = personService.findByIdThrow(personId);

        webSocketHandler.emitMessage(
            WebSocketTopic.PERSON_CREATED, WebSocketSingleResponse.builder()
                .topic(WebSocketTopic.PERSON_CREATED)
                .timestamp(ZonedDateTime.now())
                .data(personResponseMapper.toResponse(person))
                .build()
        );

        return personId;
    }

    @Builder
    public record Request(@NotNull @Valid PersonCreateRequest personCreateRequest) {

    }
}
