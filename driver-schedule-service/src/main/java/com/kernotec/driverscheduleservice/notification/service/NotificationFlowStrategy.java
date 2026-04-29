package com.kernotec.driverscheduleservice.notification.service;

import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverscheduleservice.notification.dto.NotificationFlowResponse;
import com.kernotec.driverscheduleservice.rest.dto.notification.request.NotificationSendRequest;

public interface NotificationFlowStrategy {

    CampaignRecipientEnum getFlowType();

    NotificationFlowResponse sendNotification(NotificationSendRequest request);
}
