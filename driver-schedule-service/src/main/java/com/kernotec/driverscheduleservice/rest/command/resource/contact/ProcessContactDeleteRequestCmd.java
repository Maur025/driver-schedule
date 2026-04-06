package com.kernotec.driverscheduleservice.rest.command.resource.contact;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.resource.contact.ContactToggleCmd;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessContactDeleteRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessContactDeleteRequestCmd.Request, Void>
{

    private final ContactToggleCmd contactToggleCmd;

    @Override
    protected Void run(Request request) {
        contactToggleCmd.withRequest(ContactToggleCmd.Request.builder()
                .contactId(request.contactId)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID contactId) {

    }
}
