package com.kernotec.driverschedule.notification.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.command.NotificationConfigurationDeleteCmd;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessNotificationConfigDeleteRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessNotificationConfigDeleteRequestCmd.Request, Void>
{

    private final NotificationConfigurationDeleteCmd notificationConfigurationDeleteCmd;

    @Override
    protected Void run(Request request) {
        notificationConfigurationDeleteCmd.withRequest(
                NotificationConfigurationDeleteCmd.Request.builder()
                    .notificationConfigurationId(request.notificationConfigurationId())
                    .build())
            .execute();

        return null;
    }

    @Builder
    public record Request(@NotNull UUID notificationConfigurationId) {

    }
}