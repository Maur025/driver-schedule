package com.kernotec.driverschedule.notification.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.enums.PlatformEnum;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationConfigurationCreateCmd extends
    AbstractTransactionalRequiredCommand<NotificationConfigurationCreateCmd.Request, UUID>
{

    private final NotificationConfigurationService notificationConfigurationService;

    @Override
    protected UUID run(Request request) {
        var notificationConfiguration = new NotificationConfiguration();

        notificationConfiguration.setUserId(request.userId());
        notificationConfiguration.setDeviceId(request.deviceId()
            .trim());
        notificationConfiguration.setPlatform(request.platform());
        notificationConfiguration.setToken(request.token());
        notificationConfiguration.setActived(request.isActive());
        notificationConfiguration.setPersonId(request.personId());

        notificationConfiguration = notificationConfigurationService.save(
            notificationConfiguration);
        return notificationConfiguration.getId();
    }

    @Builder
    public record Request(@NotNull UUID userId, @NotNull String deviceId,
                          @NotNull PlatformEnum platform, @NotNull String token,
                          @NotNull boolean isActive, @NotNull UUID personId)
    {

    }
}