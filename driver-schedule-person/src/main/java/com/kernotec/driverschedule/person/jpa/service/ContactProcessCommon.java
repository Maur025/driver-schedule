package com.kernotec.driverschedule.person.jpa.service;

import com.kernotec.driverschedule.person.command.ContactManyCreateCmd;
import com.kernotec.driverschedule.person.jpa.entity.Contact;
import com.kernotec.driverschedule.person.jpa.enums.ContactCategoryEnum;
import com.kernotec.driverschedule.person.rest.dto.request.ContactCreateRequest;
import com.kernotec.driverschedule.person.rest.mapper.request.ContactEntityMapper;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactProcessCommon {

    private final ContactService contactService;
    private final ContactCategoryService contactCategoryService;

    private final ContactEntityMapper contactEntityMapper;

    private final ContactManyCreateCmd contactManyCreateCmd;

    public void registerManyContactsForPerson(List<ContactCreateRequest> contactCreateRequestList,
        UUID personId, boolean isUpdate)
    {
        if (contactCreateRequestList == null || contactCreateRequestList.isEmpty()) {
            log.debug(
                "No contact create requests provided for person with id {}. Skipping contact creation.",
                personId
            );
            return;
        }

        if (isUpdate) {
            contactService.deleteAllByPersonId(personId);
        }

        UUID contactCategoryId = contactCategoryService.findIdByCodeThrow(
            ContactCategoryEnum.PHONE);

        List<Contact> contactListToSave = contactEntityMapper.toEntity(
            contactCreateRequestList, contactCategoryId, personId);

        contactManyCreateCmd.withRequest(ContactManyCreateCmd.Request.builder()
                .contactList(contactListToSave)
                .build())
            .execute();
    }

    public void registerManyContactsForPerson(List<ContactCreateRequest> contactCreateRequestList,
        UUID personId)
    {
        registerManyContactsForPerson(contactCreateRequestList, personId, false);
    }
}
