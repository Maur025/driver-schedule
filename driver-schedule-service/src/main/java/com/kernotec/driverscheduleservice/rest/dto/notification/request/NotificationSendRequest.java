package com.kernotec.driverscheduleservice.rest.dto.notification.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class NotificationSendRequest extends BaseRequest {

    private String title;
    private String body;
    private CampaignRecipientEnum campaignRecipient;
    private Set<UUID> personIds;
}
