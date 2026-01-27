package com.kernotec.driverscheduleservice.command.contact;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.Contact;
import com.kernotec.driverscheduleservice.jpa.service.ContactService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContactCreateCmd extends
    AbstractTransactionalRequiredCommand<ContactCreateCmd.Request, UUID>
{

    private final ContactService contactService;

    @Override
    protected UUID run(Request request) {
        var contact = new Contact();

        contact.setLabelTypeId(request.labelTypeId);
        contact.setValue(request.value);
        contact.setContactCategoryId(request.contactCategoryId);
        contact.setPersonId(request.personId);

        contact = contactService.save(contact);
        return contact.getId();
    }

    @Builder
    public record Request(@NotNull UUID labelTypeId, @NotNull String value,
                          @NotNull UUID contactCategoryId, @NotNull UUID personId)
    {

    }
}
