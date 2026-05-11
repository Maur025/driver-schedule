package com.kernotec.driverschedule.notification.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationConfigurationDeleteCmd extends
    AbstractTransactionalRequiredCommand<NotificationConfigurationDeleteCmd.Request, Void>
{

    private final NotificationConfigurationService notificationConfigurationService;

    @Override
    protected Void run(Request request) {
        notificationConfigurationService.findByIdThrow(request.notificationConfigurationId());

        notificationConfigurationService.deleteById(request.notificationConfigurationId());
        return null;
    }

    @Builder
    public record Request(@NotNull UUID notificationConfigurationId) {

    }
}