package com.kernotec.driverschedule.service.command.notification;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.exception.notification.PersonNotificationException;
import com.kernotec.driverschedule.service.jpa.entity.notification.PersonNotification;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import com.kernotec.driverschedule.service.jpa.service.notification.PersonNotificationService;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PersonNotificationUpdateCmd extends
    AbstractTransactionalRequiredCommand<PersonNotificationUpdateCmd.Request, Void>
{

    private final PersonNotificationService personNotificationService;

    @Override
    protected Void run(Request request) {
        PersonNotification personNotification = personNotificationService.findByIdThrow(
            request.personNotificationId());

        if (!personNotification.getPersonId()
            .equals(request.personId()))
        {
            throw new PersonNotificationException(
                "does.not.belong", "", HttpStatus.CONFLICT.value());
        }

        if (request.readAt() != null) {
            personNotification.setReadAt(request.readAt());
        }

        if (request.state() != null) {
            personNotification.setState(request.state());
        }

        personNotificationService.save(personNotification);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID personNotificationId, @NotNull UUID personId,
                          ZonedDateTime readAt, PersonNotificationState state)
    {

    }
}