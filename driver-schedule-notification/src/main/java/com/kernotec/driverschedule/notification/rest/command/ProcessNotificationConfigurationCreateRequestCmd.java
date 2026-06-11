package com.kernotec.driverschedule.notification.rest.command;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.core.exception.ResourceNotFoundException;
import com.kernotec.driverschedule.common.security.auth.SecurityAuthProvider;
import com.kernotec.driverschedule.notification.command.NotificationConfigurationCreateCmd;
import com.kernotec.driverschedule.notification.command.NotificationConfigurationUpdateCmd;
import com.kernotec.driverschedule.notification.jpa.entitiy.NotificationConfiguration;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationConfigurationCreateRequest;
import com.kernotec.driverschedule.person.jpa.service.PersonService;
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
    private final NotificationConfigurationUpdateCmd notificationConfigurationUpdateCmd;

    @Override
    protected UUID run(Request request) {
        NotificationConfigurationCreateRequest notificationConfigCreateRequest = request.notificationConfigurationCreateRequest();

        Optional<NotificationConfiguration> notificationConfigOptional = notificationConfigurationService.findByToken(
            notificationConfigCreateRequest.getToken());

        UUID personId = personService.findIdByUserIdAuthenticateThrow();
        UUID userId = securityAuthProvider.getUserId();

        if (notificationConfigOptional.isEmpty()) {
            return notificationConfigurationCreateCmd.withRequest(
                    NotificationConfigurationCreateCmd.Request.builder()
                        .userId(userId)
                        .deviceId(notificationConfigCreateRequest.getDeviceId())
                        .platform(notificationConfigCreateRequest.getPlatform())
                        .token(notificationConfigCreateRequest.getToken())
                        .isActive(true)
                        .personId(personId)
                        .build())
                .execute();
        }

        NotificationConfiguration notificationConfiguration = notificationConfigOptional.orElseThrow(
            () -> new ResourceNotFoundException("Notification configuration"));

        if (notificationConfiguration.getPersonId()
            .equals(personId))
        {
            return notificationConfiguration.getId();
        }

        notificationConfigurationUpdateCmd.withRequest(
                NotificationConfigurationUpdateCmd.Request.builder()
                    .notificationConfigurationId(notificationConfiguration.getId())
                    .personId(personId)
                    .userId(userId)
                    .build())
            .execute();

        return notificationConfiguration.getId();
    }

    @Builder
    public record Request(
        @NotNull NotificationConfigurationCreateRequest notificationConfigurationCreateRequest)
    {

    }
}