package com.kernotec.driverscheduleservice.notification.service;

import com.kernotec.core.exception.custom.base.DefaultApiException;
import com.kernotec.driverscheduleservice.jpa.dto.mapper.notification.NotificationConfigurationDtoFlatMapper;
import com.kernotec.driverscheduleservice.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationConfiguration;
import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverscheduleservice.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverscheduleservice.notification.NotificationHandler;
import com.kernotec.driverscheduleservice.notification.NotificationHandlerRequest;
import com.kernotec.driverscheduleservice.notification.NotificationHandlerResponse;
import com.kernotec.driverscheduleservice.notification.dto.NotificationFlowResponse;
import com.kernotec.driverscheduleservice.rest.dto.notification.request.NotificationSendRequest;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationOnlyUserStrategy implements NotificationFlowStrategy {

    private final NotificationHandler notificationHandler;
    private final NotificationConfigurationService notificationConfigurationService;
    private final NotificationConfigurationDtoFlatMapper notificationConfigurationDtoFlatMapper;

    @Override
    public CampaignRecipientEnum getFlowType() {
        return CampaignRecipientEnum.ONLY_USER;
    }

    @Override
    public NotificationFlowResponse sendNotification(NotificationSendRequest request) {
        if (request.getPersonIds()
            .isEmpty())
        {
            throw new DefaultApiException("PersonIds cannot be empty for ONLY_USER strategy");
        }

        UUID personId = request.getPersonIds()
            .iterator()
            .next();

        List<NotificationConfiguration> notificationConfigList = notificationConfigurationService.findByPersonIdInForNotification(
            Set.of(personId));
        List<NotificationConfigurationDto> notificationConfigDtoList = notificationConfigurationDtoFlatMapper.toDto(
            notificationConfigList);

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
                .title(request.getTitle())
                .body(request.getBody())
                .build());

        return NotificationFlowResponse.builder()
            .notificationResponse(notificationResponse)
            .notificationConfigDtoList(notificationConfigDtoList)
            .usedTokens(tokens)
            .build();
    }
}
