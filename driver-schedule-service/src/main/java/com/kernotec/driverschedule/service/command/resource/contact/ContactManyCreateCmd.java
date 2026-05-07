package com.kernotec.driverschedule.service.command.resource.contact;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.jpa.service.resource.ContactService;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactManyCreateCmd extends
    AbstractTransactionalRequiredCommand<ContactManyCreateCmd.Request, List<Contact>>
{

    private final ContactService contactService;

    @Override
    protected List<Contact> run(Request request) {
        if (request.contactList == null || request.contactList.isEmpty()) {
            log.debug("No contact list provided");
            return null;
        }

        return contactService.saveAll(request.contactList);
    }

    @Builder
    public record Request(List<Contact> contactList) {

    }
}
