package com.kernotec.driverscheduleservice.rest.command.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.exception.PersonException;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.entity.PersonType;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.PersonTypeService;
import com.kernotec.driverscheduleservice.rest.dto.request.person.PersonCreateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserCreateRequest;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.response.UserCreateResponse;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProcessPersonCreateRequestCmd extends
    AbstractCommand<ProcessPersonCreateRequestCmd.Request, UUID>
{

    private final WebSocketHandler webSocketHandler;
    private final PersonResponseMapper personResponseMapper;
    private final PersonService personService;
    private final PersonTypeService personTypeService;
    private final PersonCreateWithTypeCmd personCreateWithTypeCmd;

    @Override
    protected void validate(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;

        if (personCreateRequest.getPersonTypeIds()
            .isEmpty())
        {
            throw new PersonException("type.list.empty", "", HttpStatus.BAD_REQUEST.value());
        }

        Optional<Person> personOptional = personService.findByDocument(
            personCreateRequest.getDocument());

        if (personOptional.isPresent()) {
            throw new PersonException(
                "document.already.exists", "'" + personCreateRequest.getDocument() + "'",
                HttpStatus.CONFLICT.value()
            );
        }
    }

    @Override
    protected UUID run(Request request) {
        PersonCreateRequest personCreateRequest = request.personCreateRequest;

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

        UUID personId = personCreateWithTypeCmd.withRequest(
                PersonCreateWithTypeCmd.Request.builder()
                    .personCreateRequest(personCreateRequest)
                    .userCreateResponse(userCreateResponse)
                    .build())
            .execute();

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
