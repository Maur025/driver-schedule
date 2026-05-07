package com.kernotec.driverschedule.service.command.resource.contact;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.jpa.entity.resource.Contact;
import com.kernotec.driverschedule.service.jpa.service.resource.ContactService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactToggleCmd extends
    AbstractTransactionalRequiredCommand<ContactToggleCmd.Request, Void>
{

    private final ContactService contactService;

    @Override
    protected Void run(Request request) {
        Contact contact = contactService.findByIdThrow(request.contactId);

        if (contact.isDeleted()) {
            log.debug("Contact deleted successfully");
            return null;
        }

        contact.setDeleted(!contact.isDeleted());
        contactService.save(contact);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID contactId) {

    }
}
