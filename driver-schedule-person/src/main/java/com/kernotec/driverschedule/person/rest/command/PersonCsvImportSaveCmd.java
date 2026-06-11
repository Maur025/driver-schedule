package com.kernotec.driverschedule.person.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.LabelType;
import com.kernotec.driverschedule.person.jpa.entity.PersonType;
import com.kernotec.driverschedule.person.jpa.enums.ContactCategoryEnum;
import com.kernotec.driverschedule.person.jpa.enums.LabelTypeCodeEnum;
import com.kernotec.driverschedule.person.jpa.enums.PersonTypeEnum;
import com.kernotec.driverschedule.person.jpa.service.ContactCategoryService;
import com.kernotec.driverschedule.person.jpa.service.LabelTypeService;
import com.kernotec.driverschedule.person.jpa.service.PersonTypeService;
import com.kernotec.driverschedule.person.rest.dto.PersonCsvImportDto;
import com.kernotec.driverschedule.person.rest.dto.request.ContactCreateRequest;
import com.kernotec.driverschedule.person.rest.dto.request.PersonCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonCsvImportSaveCmd extends
    AbstractTransactionalRequiredCommand<PersonCsvImportSaveCmd.Request, Void>
{

    private final PersonTypeService personTypeService;
    private final ContactCategoryService contactCategoryService;
    private final LabelTypeService labelTypeService;
    private final ProcessPersonCreateRequestCmd processPersonCreateRequestCmd;

    @Override
    protected Void run(Request request) {
        if (request.dtoList.isEmpty()) {
            log.debug("No persons data to import.");
            return null;
        }

        Map<PersonTypeEnum, UUID> personTypeMap = personTypeService.findAll()
            .stream()
            .collect(
                Collectors.toMap(
                    personType -> PersonTypeEnum.fromValue(personType.getCode()), PersonType::getId,
                    (prev, next) -> prev
                ));

        Map<LabelTypeCodeEnum, UUID> labelTypeMap = labelTypeService.findAll()
            .stream()
            .collect(
                Collectors.toMap(
                    labelType -> LabelTypeCodeEnum.fromValue(labelType.getCode()), LabelType::getId,
                    (prev, next) -> prev
                ));

        UUID contactCategoryId = contactCategoryService.findIdByCodeThrow(
            ContactCategoryEnum.PHONE);

        List<PersonCreateRequest> personCreateRequestList = new ArrayList<>();

        for (PersonCsvImportDto personCsvImportDto : request.dtoList) {
            PersonCreateRequest personCreateRequest = getPersonCreateRequest(
                personCsvImportDto, personTypeMap, labelTypeMap, contactCategoryId);

            personCreateRequestList.add(personCreateRequest);
        }

        int index = 1;

        for (PersonCreateRequest personCreateRequest : personCreateRequestList) {
            log.info("Processing PersonCreateRequest Batch with index {}", index);

            processPersonCreateRequestCmd.withRequest(
                    ProcessPersonCreateRequestCmd.Request.builder()
                        .personCreateRequest(personCreateRequest)
                        .build())
                .execute();

            index++;
        }

        return null;
    }

    private PersonCreateRequest getPersonCreateRequest(PersonCsvImportDto personDto,
        Map<PersonTypeEnum, UUID> personTypeMap, Map<LabelTypeCodeEnum, UUID> labelTypeMap,
        UUID contactCategoryId)
    {
        var personCreateRequest = new PersonCreateRequest();

        personCreateRequest.setName(personDto.getName());
        personCreateRequest.setLastName(personDto.getLastName());
        personCreateRequest.setDocument(personDto.getDocument());
        personCreateRequest.setPersonTypeIds(Set.of(personTypeMap.get(personDto.getPersonType())));
        personCreateRequest.setUsername(personDto.getUsername());

        personCreateRequest.setContacts(
            getContactCreateRequestList(personDto, labelTypeMap, contactCategoryId));

        return personCreateRequest;
    }

    private List<ContactCreateRequest> getContactCreateRequestList(PersonCsvImportDto personDto,
        Map<LabelTypeCodeEnum, UUID> labelTypeMap, UUID contactCategoryId)
    {
        List<ContactCreateRequest> contactCreateRequestList = new ArrayList<>();

        if (personDto.getPhoneWhatsapp() != null) {
            contactCreateRequestList.add(getContactCreateRequest(
                personDto.getPhoneWhatsapp(), labelTypeMap.get(LabelTypeCodeEnum.MAIN),
                contactCategoryId
            ));
        }

        if (personDto.getPhoneWork() != null) {
            contactCreateRequestList.add(getContactCreateRequest(
                personDto.getPhoneWork(), labelTypeMap.get(LabelTypeCodeEnum.WORK),
                contactCategoryId
            ));
        }

        return contactCreateRequestList;
    }

    private ContactCreateRequest getContactCreateRequest(String contactValue, UUID labelTypeId,
        UUID contactCategoryId)
    {
        var contactCreateRequest = new ContactCreateRequest();
        contactCreateRequest.setLabelTypeId(labelTypeId);
        contactCreateRequest.setValue(contactValue);
        contactCreateRequest.setContactCategoryId(contactCategoryId);

        return contactCreateRequest;
    }

    @Builder
    public record Request(@NotNull List<PersonCsvImportDto> dtoList) {

    }
}
