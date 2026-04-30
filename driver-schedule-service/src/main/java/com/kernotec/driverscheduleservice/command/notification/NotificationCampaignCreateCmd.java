package com.kernotec.driverscheduleservice.command.notification;

import com.kernotec.core.command.AbstractTransactionalRequiredCommand;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationCampaign;
import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverscheduleservice.jpa.service.notification.NotificationCampaignService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationCampaignCreateCmd extends
    AbstractTransactionalRequiredCommand<NotificationCampaignCreateCmd.Request, UUID>
{

    private final NotificationCampaignService notificationCampaignService;

    @Override
    protected UUID run(Request request) {
        var notificationCampaign = new NotificationCampaign();

        notificationCampaign.setTitle(request.title());
        notificationCampaign.setBody(request.body());
        notificationCampaign.setCampaignRecipient(request.campaignRecipient());
        notificationCampaign.setPersonIds(request.personIds());

        notificationCampaign = notificationCampaignService.save(notificationCampaign);
        return notificationCampaign.getId();
    }

    @Builder
    public record Request(@NotNull String title, @NotNull String body,
                          @NotNull CampaignRecipientEnum campaignRecipient, String personIds)
    {

    }
}