package com.kernotec.driverscheduleservice.command.resource.contact;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.Contact;
import com.kernotec.driverscheduleservice.jpa.service.resource.ContactService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContactUpdateCmd extends
    AbstractTransactionalRequiredCommand<ContactUpdateCmd.Request, Void>
{

    private final ContactService contactService;

    @Override
    protected Void run(Request request) {
        Contact contact = contactService.findByIdThrow(request.contactId);

        if (request.labelTypeId != null) {
            contact.setLabelTypeId(request.labelTypeId);
        }
        if (request.value != null) {
            contact.setValue(request.value);
        }
        if (request.contactCategoryId != null) {
            contact.setContactCategoryId(request.contactCategoryId);
        }

        contactService.save(contact);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID contactId, UUID labelTypeId, String value,
                          UUID contactCategoryId)
    {

    }
}
