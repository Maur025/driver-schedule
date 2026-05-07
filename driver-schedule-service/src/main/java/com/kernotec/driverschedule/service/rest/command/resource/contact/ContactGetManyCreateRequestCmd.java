package com.kernotec.driverschedule.service.rest.command.resource.contact;

import com.kernotec.core.command.AbstractCommand;
import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.rest.dto.resource.request.contact.ContactCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactGetManyCreateRequestCmd extends
    AbstractCommand<ContactGetManyCreateRequestCmd.Request, List<Contact>>
{

    @Override
    protected List<Contact> run(Request request) {
        if (request.contactCreateRequestList == null
            || request.contactCreateRequestList.isEmpty())
        {
            log.debug("No contact create request found");
            return List.of();
        }

        List<Contact> contactList = new ArrayList<>();

        for (ContactCreateRequest contactCreateRequest : request.contactCreateRequestList) {
            var contact = new Contact();

            contact.setLabelTypeId(contactCreateRequest.getLabelTypeId());
            contact.setValue(contactCreateRequest.getValue());
            contact.setContactCategoryId(request.contactCategoryId);
            contact.setPersonId(request.personId);

            contactList.add(contact);
        }

        return contactList;
    }

    @Builder
    public record Request(List<ContactCreateRequest> contactCreateRequestList,
                          @NotNull UUID contactCategoryId, @NotNull UUID personId)
    {

    }
}
