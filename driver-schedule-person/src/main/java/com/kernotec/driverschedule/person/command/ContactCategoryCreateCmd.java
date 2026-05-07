package com.kernotec.driverschedule.person.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.person.jpa.entity.ContactCategory;
import com.kernotec.driverschedule.person.jpa.service.ContactCategoryService;
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
