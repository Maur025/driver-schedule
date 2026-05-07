package com.kernotec.driverschedule.service.rest.command.notification;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverschedule.service.command.notification.NotificationConfigurationCreateCmd;
import com.kernotec.driverschedule.service.common.security.SecurityAuthProvider;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverschedule.service.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverschedule.service.jpa.service.resource.PersonService;
import com.kernotec.driverschedule.service.rest.dto.notification.request.NotificationConfigurationCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessNotificationConfigurationCreateRequestCmd extends
    AbstractTransactionalRequiredCommand<ProcessNotificationConfigurationCreateRequestCmd.Request, UUID>
{

    private final PersonService personService;
    private final SecurityAuthProvider securityAuthProvider;
    private final NotificationConfigurationCreateCmd notificationConfigurationCreateCmd;
    private final NotificationConfigurationService notificationConfigurationService;

    @Override
    protected UUID run(Request request) {
        NotificationConfigurationCreateRequest notificationConfigCreateRequest = request.notificationConfigurationCreateRequest();

        Optional<NotificationConfiguration> notificationConfigOptional = notificationConfigurationService.findByToken(
            notificationConfigCreateRequest.getToken());

        UUID personId = personService.findIdByUserIdAuthenticateThrow();
        UUID userId = securityAuthProvider.getUserId();

        return notificationConfigOptional.map(NotificationConfiguration::getId)
            .orElseGet(() -> notificationConfigurationCreateCmd.withRequest(
                    NotificationConfigurationCreateCmd.Request.builder()
                        .userId(userId)
                        .deviceId(notificationConfigCreateRequest.getDeviceId())
                        .platform(notificationConfigCreateRequest.getPlatform())
                        .token(notificationConfigCreateRequest.getToken())
                        .isActive(true)
                        .personId(personId)
                        .build())
                .execute());
    }

    @Builder
    public record Request(
        @NotNull NotificationConfigurationCreateRequest notificationConfigurationCreateRequest)
    {

    }
}