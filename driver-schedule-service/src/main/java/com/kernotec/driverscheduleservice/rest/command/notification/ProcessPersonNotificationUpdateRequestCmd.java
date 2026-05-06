package com.kernotec.driverscheduleservice.rest.command.notification;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.command.notification.PersonNotificationUpdateCmd;
import com.kernotec.driverscheduleservice.jpa.enums.notification.PersonNotificationState;
import com.kernotec.driverscheduleservice.jpa.service.resource.PersonService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessPersonNotificationUpdateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessPersonNotificationUpdateRequestCmd.Request, Void>
{

    private final PersonNotificationUpdateCmd personNotificationUpdateCmd;
    private final PersonService personService;

    @Override
    protected Void run(Request request) {
        UUID personId = personService.findIdByUserIdAuthenticateThrow();

        personNotificationUpdateCmd.withRequest(PersonNotificationUpdateCmd.Request.builder()
                .personNotificationId(request.personNotificationId())
                .personId(personId)
                .readAt(ZonedDateTime.now())
                .state(PersonNotificationState.READ)
                .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID personNotificationId) {

    }
}