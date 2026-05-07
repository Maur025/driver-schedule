package com.kernotec.driverschedule.service.notification.service;

import com.kernotec.driverschedule.service.command.notification.NotificationCampaignCreateCmd;
import com.kernotec.driverschedule.service.command.notification.NotificationLogManyCreateCmd;
import com.kernotec.driverschedule.service.command.notification.PersonNotificationManyCreateCmd;
import com.kernotec.driverschedule.service.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverschedule.service.jpa.entity.notification.NotificationLog;
import com.kernotec.driverschedule.service.jpa.entity.notification.PersonNotification;
import com.kernotec.driverschedule.service.jpa.enums.notification.NotificationLogStateEnum;
import com.kernotec.driverschedule.service.jpa.enums.notification.PersonNotificationState;
import com.kernotec.driverschedule.service.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverschedule.service.notification.NotificationHandlerResponse;
import com.kernotec.driverschedule.service.notification.SendResponse;
import com.kernotec.driverschedule.service.notification.dto.NotificationFlowResponse;
import com.kernotec.driverschedule.service.notification.dto.NotificationSendRequest;
import com.kernotec.driverschedule.service.notification.enums.NotificationErrorCode;
import com.kernotec.driverschedule.service.rest.mapper.notification.request.NotificationLogEntityMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationOrchestrator {

    private final NotificationConfigurationService notificationConfigurationService;
    private final NotificationLogEntityMapper notificationLogEntityMapper;

    private final NotificationCampaignCreateCmd notificationCampaignCreateCmd;
    private final NotificationLogManyCreateCmd notificationLogManyCreateCmd;

    private final NotificationFlowFactory notificationFlowFactory;
    private final PersonNotificationManyCreateCmd personNotificationManyCreateCmd;

    @Async("notificationExecutor")
    public void sendAsyncNotification(NotificationSendRequest request) {
        NotificationFlowStrategy flowStrategy = notificationFlowFactory.getStrategy(
            request.campaignRecipient());

        UUID messageId = UUID.randomUUID();

        UUID notificationCampaignId = notificationCampaignCreateCmd.withRequest(
                NotificationCampaignCreateCmd.Request.builder()
                    .title(request.title())
                    .body(request.body())
                    .campaignRecipient(request.campaignRecipient())
                    .personIds(request.personIds() == null ? null : Arrays.toString(request.personIds()
                        .toArray()))
                    .build())
            .execute();

        NotificationFlowResponse notificationFlowResponse = flowStrategy.sendNotification(
            request, messageId);

        if (notificationFlowResponse == null) {
            log.warn(
                "No notification flow response received, it's understood that nothing was done.");
            return;
        }

        registerPersonNotifications(notificationFlowResponse.personIds(), request, messageId);

        Map<String, NotificationConfigurationDto> notificationConfigDtoMap = notificationFlowResponse.notificationConfigDtoList()
            .stream()
            .collect(
                Collectors.toMap(
                    NotificationConfigurationDto::getToken, config -> config,
                    (current, next) -> current
                ));

        NotificationHandlerResponse notificationResponse = notificationFlowResponse.notificationResponse();

        log.info(
            "Send notification [success]:{} | [fails]: {} | [is_succesfull]: {}",
            notificationResponse.successCount(), notificationResponse.failureCount(),
            notificationResponse.allSuccess()
        );

        List<NotificationLog> notificationLogList = notificationFlowResponse.usedTokens()
            .stream()
            .map(token -> {
                NotificationConfigurationDto notificationConfigDto = notificationConfigDtoMap.get(
                    token);

                return notificationLogEntityMapper.toEntity(
                    notificationConfigDto,
                    NotificationLogStateEnum.SENT, ZonedDateTime.now(), notificationCampaignId
                );
            })
            .toList();

        handleErrors(notificationLogList, notificationFlowResponse, notificationConfigDtoMap);

        notificationLogManyCreateCmd.withRequest(NotificationLogManyCreateCmd.Request.builder()
                .notificationLogList(notificationLogList)
                .build())
            .execute();
    }

    private void registerPersonNotifications(Set<UUID> personIds, NotificationSendRequest request,
        UUID messageId)
    {
        if (personIds == null || personIds.isEmpty()) {
            log.debug("No person ids to register notifications");
            return;
        }

        List<PersonNotification> personNotificationList = new ArrayList<>();

        for (UUID personId : personIds) {
            var personNotification = new PersonNotification();

            personNotification.setTitle(request.title());
            personNotification.setBody(request.body());
            personNotification.setData(
                request.dataMap() == null ? new HashMap<>() : request.dataMap());
            personNotification.setSentAt(ZonedDateTime.now());
            personNotification.setState(PersonNotificationState.CREATED);
            personNotification.setPersonId(personId);
            personNotification.setMessageId(messageId);

            personNotificationList.add(personNotification);
        }

        personNotificationManyCreateCmd.withRequest(
                PersonNotificationManyCreateCmd.Request.builder()
                    .personNotificationList(personNotificationList)
                    .build())
            .execute();
    }

    private void handleErrors(List<NotificationLog> notificationLogList,
        NotificationFlowResponse notificationFlowResponse,
        Map<String, NotificationConfigurationDto> notificationConfigDtoMap)
    {
        NotificationHandlerResponse notificationResponse = notificationFlowResponse.notificationResponse();

        if (notificationResponse.allSuccess()) {
            return;
        }

        Map<String, NotificationLog> notificationLogMap = notificationLogList.stream()
            .collect(Collectors.toMap(NotificationLog::getToken, log -> log));

        SendResponse[] responseList = notificationResponse.responses()
            .toArray(SendResponse[]::new);

        String[] usedTokens = notificationFlowResponse.usedTokens()
            .toArray(String[]::new);

        Set<UUID> deleteTokenIds = new HashSet<>();

        for (int i = 0; i < responseList.length; i++) {
            if (responseList[i].isSuccessful()) {
                continue;
            }

            String token = usedTokens[i];
            NotificationErrorCode errorCode = responseList[i].notificationErrorCode();

            NotificationLog notificationLog = notificationLogMap.get(token);
            notificationLog.setNotificationLogState(NotificationLogStateEnum.PENDING);

            if (errorCode.equals(NotificationErrorCode.INVALID_ARGUMENT) || errorCode.equals(
                NotificationErrorCode.UNREGISTERED))
            {
                notificationLog.setNotificationLogState(NotificationLogStateEnum.TOKEN_INVALID);

                NotificationConfigurationDto notificationConfigDto = notificationConfigDtoMap.get(
                    token);

                deleteTokenIds.add(notificationConfigDto.getId());
            }

        }

        if (!deleteTokenIds.isEmpty()) {
            notificationConfigurationService.deleteAllByIdIn(deleteTokenIds);
            log.info("Removing {} notification configurations by tokens invalids", deleteTokenIds);
        }

    }
}
