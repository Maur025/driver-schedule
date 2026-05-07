package com.kernotec.driverschedule.service.notification.dto;

import com.kernotec.driverschedule.service.jpa.enums.notification.CampaignRecipientEnum;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
public record NotificationSendRequest(String title, String body,
                                      CampaignRecipientEnum campaignRecipient, Set<UUID> personIds,
                                      Map<String, String> dataMap)
{

}
