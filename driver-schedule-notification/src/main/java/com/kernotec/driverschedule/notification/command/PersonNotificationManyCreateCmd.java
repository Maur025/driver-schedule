package com.kernotec.driverschedule.notification.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.jpa.entitiy.PersonNotification;
import com.kernotec.driverschedule.notification.jpa.service.PersonNotificationService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PersonNotificationManyCreateCmd extends
    AbstractTransactionalRequiredCommand<PersonNotificationManyCreateCmd.Request, List<PersonNotification>>
{

    private final PersonNotificationService personNotificationService;

    @Override
    protected List<PersonNotification> run(Request request) {
        if (request.personNotificationList()
            .isEmpty())
        {
            log.debug("No person notification to create ... skiping");
            return null;
        }

        return personNotificationService.saveAll(request.personNotificationList());
    }

    @Builder
    public record Request(@NotNull List<PersonNotification> personNotificationList) {

    }
}