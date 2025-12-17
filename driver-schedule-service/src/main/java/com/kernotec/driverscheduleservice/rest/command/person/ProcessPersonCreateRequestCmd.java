package com.kernotec.driverscheduleservice.rest.command.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.person.PersonCreateCmd;
import com.kernotec.driverscheduleservice.command.person.assign.type.PersonAssignTypeCreateCmd;
import com.kernotec.driverscheduleservice.exception.PersonException;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.entity.PersonType;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.PersonTypeService;
import com.kernotec.driverscheduleservice.rest.dto.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessPersonCreateRequestCmd extends
    AbstractCommand<ProcessPersonCreateRequestCmd.Request, UUID>
{

    private final PersonCreateCmd personCreateCmd;
    private final WebSocketHandler webSocketHandler;
    private final PersonResponseMapper personResponseMapper;
    private final PersonService personService;
    private final PersonTypeService personTypeService;
    private final PersonAssignTypeCreateCmd personAssignTypeCreateCmd;

    @Override
    protected UUID run(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;

        if (personCreateRequest.getPersonTypeIds()
            .isEmpty())
        {
            throw new PersonException("Person must have at least one person type");
        }

        Set<String> personTypeNames = new HashSet<>();

        for (UUID personTypeId : personCreateRequest.getPersonTypeIds()) {
            PersonType personType = personTypeService.findByIdThrow(personTypeId);
            personTypeNames.add(personType.getCode());
        }

        UserCreateResponse userCreateResponse = personService.saveUserFromPerson(
            UserCreateRequest.builder()
                .name(personCreateRequest.getName())
                .lastName(personCreateRequest.getLastName())
                .username(personCreateRequest.getUsername())
                .password(personCreateRequest.getDocument())
                .realmName("driver-schedule-auth")
                .resource("driver-schedule")
                .roles(personTypeNames)
                .build());

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
    public record Request(@NotNull PersonCreateRequest personCreateRequest) {

    }
}
