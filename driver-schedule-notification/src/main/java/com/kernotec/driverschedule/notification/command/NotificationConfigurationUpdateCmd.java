package com.kernotec.driverschedule.notification.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationConfigurationUpdateCmd extends
    AbstractTransactionalRequiredCommand<NotificationConfigurationUpdateCmd.Request, Void>
{

    private final NotificationConfigurationService notificationConfigurationService;

    @Override
    protected Void run(Request request) {
        NotificationConfiguration notificationConfiguration = notificationConfigurationService.findByIdThrow(
            request.notificationConfigurationId());

        if (request.personId() != null) {
            notificationConfiguration.setPersonId(request.personId());
        }

        if (request.userId() != null) {
            notificationConfiguration.setUserId(request.userId());
        }

        if (request.actived() != null) {
            notificationConfiguration.setActived(request.actived());
        }

        notificationConfigurationService.save(notificationConfiguration);
        return null;
    }

    @Builder
    public record Request(@NotNull UUID notificationConfigurationId, UUID personId, UUID userId,
                          Boolean actived)
    {

    }
}