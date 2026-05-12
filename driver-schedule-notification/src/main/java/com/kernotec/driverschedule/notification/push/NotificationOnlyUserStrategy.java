package com.kernotec.driverschedule.notification.push;

import com.kernotec.core.exception.custom.base.DefaultApiException;
import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.handler.NotificationHandler;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class NotificationOnlyUserStrategy extends NotificationFlowStrategy {

    public NotificationOnlyUserStrategy(
        NotificationConfigurationService notificationConfigurationService,
        NotificationHandler notificationHandler)
    {
        super(notificationConfigurationService, notificationHandler);
    }

    @Override
    public CampaignRecipientEnum getFlowType() {
        return CampaignRecipientEnum.ONLY_USER;
    }

    @Override
    protected Set<UUID> getPersonIds(NotificationSendRequest request) {
        if (request.personIds() == null || request.personIds()
            .isEmpty())
        {
            throw new DefaultApiException("PersonIds cannot be empty for ONLY_USER strategy");
        }

        UUID personId = request.personIds()
            .iterator()
            .next();

        return Set.of(personId);
    }
}
