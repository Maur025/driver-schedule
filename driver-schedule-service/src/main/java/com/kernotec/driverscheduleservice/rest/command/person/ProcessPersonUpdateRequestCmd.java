package com.kernotec.driverscheduleservice.rest.command.person;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverscheduleservice.command.person.PersonGetDtoCmd;
import com.kernotec.driverscheduleservice.command.person.PersonUpdateCmd;
import com.kernotec.driverscheduleservice.command.person.assign.type.PersonAssignTypeManyCreateCmd;
import com.kernotec.driverscheduleservice.config.AuthConfigProperties;
import com.kernotec.driverscheduleservice.jpa.dto.PersonDto;
import com.kernotec.driverscheduleservice.jpa.entity.Person;
import com.kernotec.driverscheduleservice.jpa.entity.PersonAssignType;
import com.kernotec.driverscheduleservice.jpa.service.PersonAssignTypeService;
import com.kernotec.driverscheduleservice.jpa.service.PersonService;
import com.kernotec.driverscheduleservice.jpa.service.PersonTypeService;
import com.kernotec.driverscheduleservice.rest.command.person.assign.type.PersonAssignTypeGetManyRequestCmd;
import com.kernotec.driverscheduleservice.rest.dto.request.person.PersonUpdateRequest;
import com.kernotec.driverscheduleservice.rest.dto.response.person.PersonResponse;
import com.kernotec.driverscheduleservice.rest.dto.response.web.socket.WebSocketSingleResponse;
import com.kernotec.driverscheduleservice.rest.mapper.response.person.PersonResponseMapper;
import com.kernotec.driverscheduleservice.web.socket.WebSocketHandler;
import com.kernotec.driverscheduleservice.web.socket.WebSocketTopic;
import com.kernotec.driverscheduleservice.webflux.user.spec.rest.dto.request.UserUpdateRequest;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessPersonUpdateRequestCmd extends
    AbstractCommand<ProcessPersonUpdateRequestCmd.Request, Void>
{

    private final AuthConfigProperties authConfigProperties;

    private final PersonTypeService personTypeService;
    private final PersonService personService;
    private final PersonAssignTypeService personAssignTypeService;

    private final PersonResponseMapper personResponseMapper;

    private final PersonValidationCmd personValidationCmd;
    private final PersonGetDtoCmd personGetDtoCmd;
    private final PersonUpdateCmd personUpdateCmd;
    private final PersonAssignTypeGetManyRequestCmd personAssignTypeGetManyRequestCmd;
    private final PersonAssignTypeManyCreateCmd personAssignTypeManyCreateCmd;
    private final WebSocketHandler webSocketHandler;

    @Override
    protected void validate(Request request) {
        PersonUpdateRequest personUpdateRequest = request.personUpdateRequest;

        personValidationCmd.withRequest(PersonValidationCmd.Request.builder()
                .personTypeIdSet(personUpdateRequest.getPersonTypeIds())
                .document(personUpdateRequest.getDocument())
                .personId(request.personId)
                .build())
            .execute();
    }

    @Override
    protected Void run(Request request) {
        PersonUpdateRequest personUpdateRequest = request.personUpdateRequest;

        PersonDto personDto = personGetDtoCmd.withRequest(PersonGetDtoCmd.Request.builder()
                .personId(request.personId)
                .build())
            .execute();

        Set<String> personTypeCodes = null;

        if (personUpdateRequest.getPersonTypeIds() != null) {
            personTypeCodes = personTypeService.getCodesOfPersonTypeIds(
                personUpdateRequest.getPersonTypeIds());
        }

        personService.updateUserFromPerson(
            personDto.getUserId(), UserUpdateRequest.builder()
                .name(personUpdateRequest.getName())
                .lastName(personUpdateRequest.getLastName())
                .roles(personTypeCodes)
                .username(null)
                .resource(authConfigProperties.getResource())
                .build()
        );

        personUpdateCmd.withRequest(PersonUpdateCmd.Request.builder()
                .personId(request.personId)
                .name(personUpdateRequest.getName())
                .lastName(personUpdateRequest.getLastName())
                .document(personUpdateRequest.getDocument())
                .build())
            .execute();

        if (personUpdateRequest.getPersonTypeIds() != null) {
            personAssignTypeService.deleteAllByPersonId(request.personId);

            List<PersonAssignType> personAssignTypeListToSave = personAssignTypeGetManyRequestCmd.withRequest(
                    PersonAssignTypeGetManyRequestCmd.Request.builder()
                        .personTypeIdSet(personUpdateRequest.getPersonTypeIds())
                        .personId(request.personId)
                        .build())
                .execute();

            personAssignTypeManyCreateCmd.withRequest(
                    PersonAssignTypeManyCreateCmd.Request.builder()
                        .personAssignTypeList(personAssignTypeListToSave)
                        .build())
                .execute();
        }

        Person person = personService.findByIdThrow(request.personId);

        webSocketHandler.emitMessage(
            WebSocketTopic.PERSON_UPDATED, WebSocketSingleResponse.<PersonResponse>builder()
                .topic(WebSocketTopic.PERSON_UPDATED)
                .timestamp(ZonedDateTime.now())
                .data(personResponseMapper.toResponse(person))
                .build()
        );

        return null;
    }

    @Builder
    public record Request(@NotNull UUID personId,
                          @NotNull PersonUpdateRequest personUpdateRequest)
    {

    }
}
