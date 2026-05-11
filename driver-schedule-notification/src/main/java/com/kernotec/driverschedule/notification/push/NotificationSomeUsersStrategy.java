package com.kernotec.driverschedule.notification.push;

import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.handler.NotificationHandler;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
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
