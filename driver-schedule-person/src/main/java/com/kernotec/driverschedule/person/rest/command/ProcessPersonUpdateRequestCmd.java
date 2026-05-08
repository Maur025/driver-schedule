package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.common.properties.AuthConfigProperties;
import com.kernotec.driverschedule.person.command.PersonAssignTypeManyCreateCmd;
import com.kernotec.driverschedule.person.command.PersonGetDtoCmd;
import com.kernotec.driverschedule.person.command.PersonUpdateCmd;
import com.kernotec.driverschedule.person.jpa.dto.PersonDto;
import com.kernotec.driverschedule.person.jpa.entity.PersonAssignType;
import com.kernotec.driverschedule.person.jpa.service.ContactProcessCommon;
import com.kernotec.driverschedule.person.jpa.service.PersonAssignTypeService;
import com.kernotec.driverschedule.person.jpa.service.PersonTypeService;
import com.kernotec.driverschedule.person.jpa.service.UserWebFluxService;
import com.kernotec.driverschedule.person.rest.dto.request.PersonUpdateRequest;
import com.kernotec.driverschedule.person.socket.PersonSocketHandler;
import com.kernotec.driverschedule.person.socket.PersonSocketTopic;
import com.kernotec.driverschedule.user.api.spec.rest.dto.request.UserUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    private final PersonAssignTypeService personAssignTypeService;

    private final PersonValidationCmd personValidationCmd;
    private final PersonGetDtoCmd personGetDtoCmd;
    private final PersonUpdateCmd personUpdateCmd;
    private final PersonAssignTypeGetManyRequestCmd personAssignTypeGetManyRequestCmd;
    private final PersonAssignTypeManyCreateCmd personAssignTypeManyCreateCmd;
    private final ContactProcessCommon contactProcessCommon;

    private final PersonSocketHandler personSocketHandler;
    private final UserWebFluxService userWebFluxService;

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

        userWebFluxService.updateUserFromPerson(
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

        contactProcessCommon.registerManyContactsForPerson(
            personUpdateRequest.getContacts(), request.personId, true);

        personSocketHandler.emitMessage(PersonSocketHandler.Request.builder()
            .personId(request.personId())
            .topic(PersonSocketTopic.PERSON_UPDATED)
            .build());

        return null;
    }

    @Builder
    public record Request(@NotNull UUID personId,
                          @NotNull @Valid PersonUpdateRequest personUpdateRequest)
    {

    }
}
