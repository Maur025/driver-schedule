package com.kernotec.driverscheduleservice.command.resource.contact.category;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.resource.ContactCategory;
import com.kernotec.driverscheduleservice.jpa.service.resource.ContactCategoryService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ContactCategoryUpdateCmd extends
    AbstractTransactionalRequiredCommand<ContactCategoryUpdateCmd.Request, Void>
{

    private final ContactCategoryService contactCategoryService;

    @Override
    protected Void run(Request request) {
        ContactCategory contactCategory = contactCategoryService.findByIdThrow(
            request.contactCategoryId);

        if (request.name != null) {
            contactCategory.setName(request.name);
        }

        contactCategory = contactCategoryService.save(contactCategory);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID contactCategoryId, String name) {

    }
}
