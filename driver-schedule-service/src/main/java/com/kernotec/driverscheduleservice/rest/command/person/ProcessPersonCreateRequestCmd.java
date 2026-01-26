package com.kernotec.driverscheduleservice.rest.command.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.contact.ContactManyCreateCmd;
import com.kernotec.driverscheduleservice.config.AuthConfigProperties;
import com.kernotec.driverscheduleservice.jpa.entity.Contact;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.enums.ContactCategoryEnum;
import com.kernotec.driverscheduleservice.jpa.service.ContactCategoryService;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.PersonTypeService;
import com.kernotec.driverscheduleservice.rest.command.contact.ContactGetManyCreateRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.contact.ContactCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
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
    private final ContactCategoryService contactCategoryService;
    private final ContactGetManyCreateRequestCmd contactGetManyCreateRequestCmd;
    private final ContactManyCreateCmd contactManyCreateCmd;

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

        registerContacts(personCreateRequest.getContacts(), personId);

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

    private void registerContacts(List<ContactCreateRequest> contactCreateRequestList,
        UUID personId)
    {
        if (contactCreateRequestList == null || contactCreateRequestList.isEmpty()) {
            log.debug("No contacts to register");
            return;
        }

        UUID contactCategoryId = contactCategoryService.findIdByCodeThrow(
            ContactCategoryEnum.PHONE);

        List<Contact> contactListToSave = contactGetManyCreateRequestCmd.withRequest(
                ContactGetManyCreateRequestCmd.Request.builder()
                    .contactCreateRequestList(contactCreateRequestList)
                    .contactCategoryId(contactCategoryId)
                    .personId(personId)
                    .build())
            .execute();

        contactManyCreateCmd.withRequest(ContactManyCreateCmd.Request.builder()
                .contactList(contactListToSave)
                .build())
            .execute();
    }

    @Builder
    public record Request(@NotNull @Valid PersonCreateRequest personCreateRequest) {

    }
}
