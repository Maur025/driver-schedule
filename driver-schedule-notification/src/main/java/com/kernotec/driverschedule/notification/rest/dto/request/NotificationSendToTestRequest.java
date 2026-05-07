package com.kernotec.driverschedule.notification.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kernotec.core.rest.dto.request.BaseRequest;
import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class NotificationSendToTestRequest extends BaseRequest {

    private String title;
    private String body;
    private CampaignRecipientEnum campaignRecipient;
    private Set<UUID> personIds;
    private Map<String, String> dataMap;
}