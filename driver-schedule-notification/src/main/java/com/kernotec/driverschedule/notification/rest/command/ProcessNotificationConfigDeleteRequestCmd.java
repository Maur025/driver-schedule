package com.kernotec.driverschedule.notification.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.command.NotificationConfigurationDeleteCmd;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessNotificationConfigDeleteRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessNotificationConfigDeleteRequestCmd.Request, Void>
{

    private final NotificationConfigurationDeleteCmd notificationConfigurationDeleteCmd;
    private final NotificationConfigurationService notificationConfigurationService;

    @Override
    protected Void run(Request request) {
        NotificationConfiguration notificationConfiguration = notificationConfigurationService.findByTokenThrow(
            request.token());

        notificationConfigurationDeleteCmd.withRequest(
                NotificationConfigurationDeleteCmd.Request.builder()
                    .notificationConfigurationId(notificationConfiguration.getId())
                    .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull String token) {

    }
}