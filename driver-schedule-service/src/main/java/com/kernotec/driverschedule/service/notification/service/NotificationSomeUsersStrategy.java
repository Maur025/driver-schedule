package com.kernotec.driverschedule.service.notification.service;

import com.kernotec.driverschedule.service.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverschedule.service.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverschedule.service.notification.NotificationHandler;
import com.kernotec.driverschedule.service.notification.dto.NotificationSendRequest;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationSomeUsersStrategy extends NotificationFlowStrategy {

    public NotificationSomeUsersStrategy(
        NotificationConfigurationService notificationConfigurationService,
        NotificationHandler notificationHandler)
    {
        super(notificationConfigurationService, notificationHandler);
    }

    @Override
    protected CampaignRecipientEnum getFlowType() {
        return CampaignRecipientEnum.SOME_USERS;
    }

    @Override
    protected Set<UUID> getPersonIds(NotificationSendRequest request) {
        if (request.personIds() == null || request.personIds()
            .isEmpty())
        {
            log.debug("No person selected ... skiping");
            return null;
        }

        return request.personIds();
    }
}
