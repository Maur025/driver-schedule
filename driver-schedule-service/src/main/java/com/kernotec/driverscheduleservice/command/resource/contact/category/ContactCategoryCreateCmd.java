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
public class ContactCategoryCreateCmd extends
    AbstractTransactionalRequiredCommand<ContactCategoryCreateCmd.Request, UUID>
{

    private final ContactCategoryService contactCategoryService;

    @Override
    protected UUID run(Request request) {
        var contactCategory = new ContactCategory();

        contactCategory.setName(request.name);
        contactCategory.setCode(request.code);

        contactCategory = contactCategoryService.save(contactCategory);
        return contactCategory.getId();
    }

    @Builder
    public record Request(@NotNull String name, @NotNull String code) {

    }
}
