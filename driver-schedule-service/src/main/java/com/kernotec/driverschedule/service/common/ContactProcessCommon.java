package com.kernotec.driverschedule.service.common;

import com.kernotec.driverschedule.service.command.resource.contact.ContactManyCreateCmd;
import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.jpa.enums.resource.ContactCategoryEnum;
import com.kernotec.driverschedule.service.jpa.service.resource.ContactCategoryService;
import com.kernotec.driverschedule.service.jpa.service.resource.ContactService;
import com.kernotec.driverschedule.service.rest.dto.resource.request.contact.ContactCreateRequest;
import com.kernotec.driverschedule.service.rest.mapper.resource.request.contact.ContactEntityMapper;
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
