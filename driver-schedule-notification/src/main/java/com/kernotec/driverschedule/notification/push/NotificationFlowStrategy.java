package com.kernotec.driverschedule.notification.push;

import com.kernotec.driverschedule.notification.jpa.dto.NotificationConfigurationDto;
import com.kernotec.driverschedule.notification.jpa.enums.CampaignRecipientEnum;
import com.kernotec.driverschedule.notification.jpa.service.NotificationConfigurationService;
import com.kernotec.driverschedule.notification.handler.NotificationHandler;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationHandlerRequest;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationHandlerResponse;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationFlowResponse;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NotificationFlowStrategy {

    private final NotificationConfigurationService notificationConfigurationService;
    private final NotificationHandler notificationHandler;

    public NotificationFlowStrategy(NotificationConfigurationService notificationService,
        NotificationHandler notificationHandler)
    {
        this.notificationConfigurationService = notificationService;
        this.notificationHandler = notificationHandler;
    }

    protected abstract CampaignRecipientEnum getFlowType();

    protected abstract Set<UUID> getPersonIds(NotificationSendRequest request);

    public NotificationFlowResponse sendNotification(NotificationSendRequest request,
        UUID messageId)
    {
        Set<UUID> personIds = getPersonIds(request);

        if (personIds == null) {
            return null;
        }

        List<NotificationConfigurationDto> notificationConfigDtoList = notificationConfigurationService.findDtoByPersonIdInForNotification(
            personIds);

        Set<String> tokens = notificationConfigDtoList.stream()
            .map(NotificationConfigurationDto::getToken)
            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (tokens.isEmpty()) {
            log.warn("No tokens found for notification... skipping");
            return null;
        }

        NotificationHandlerResponse notificationResponse = notificationHandler.pushNotification(
            NotificationHandlerRequest.builder()
                .tokens(tokens)
                .title(request.title())
                .body(request.body())
                .dataMap(getDataMap(request.dataMap(), messageId))
                .build());

        return NotificationFlowResponse.builder()
            .notificationResponse(notificationResponse)
            .notificationConfigDtoList(notificationConfigDtoList)
            .usedTokens(tokens)
            .personIds(personIds)
            .build();
    }

    private Map<String, String> getDataMap(Map<String, String> beforeDataMap, UUID messageId) {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("messageId", String.valueOf(messageId));

        if (beforeDataMap != null) {
            dataMap.putAll(beforeDataMap);
        }

        return dataMap;
    }
}
