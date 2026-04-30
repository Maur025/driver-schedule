package com.kernotec.driverscheduleservice.notification.service;

import com.kernotec.driverscheduleservice.command.notification.NotificationCampaignCreateCmd;
import com.kernotec.driverscheduleservice.command.notification.NotificationLogManyCreateCmd;
import com.kernotec.driverscheduleservice.jpa.dto.notification.NotificationConfigurationDto;
import com.kernotec.driverscheduleservice.jpa.entity.notification.NotificationLog;
import com.kernotec.driverscheduleservice.jpa.enums.notification.CampaignRecipientEnum;
import com.kernotec.driverscheduleservice.jpa.enums.notification.NotificationLogStateEnum;
import com.kernotec.driverscheduleservice.jpa.service.notification.NotificationConfigurationService;
import com.kernotec.driverscheduleservice.notification.NotificationHandlerResponse;
import com.kernotec.driverscheduleservice.notification.SendResponse;
import com.kernotec.driverscheduleservice.notification.dto.NotificationFlowResponse;
import com.kernotec.driverscheduleservice.notification.dto.NotificationSendRequest;
import com.kernotec.driverscheduleservice.notification.enums.NotificationErrorCode;
import com.kernotec.driverscheduleservice.rest.mapper.notification.request.NotificationLogEntityMapper;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Builder;
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

    @Async("notificationExecutor")
    public void sendAsyncNotification(Request request) {
        NotificationSendRequest notificationSendRequest = request.notificationSendRequest();

        NotificationFlowStrategy flowStrategy = notificationFlowFactory.getStrategy(
            CampaignRecipientEnum.ONLY_USER);

        UUID notificationCampaignId = notificationCampaignCreateCmd.withRequest(
                NotificationCampaignCreateCmd.Request.builder()
                    .title(notificationSendRequest.title())
                    .body(notificationSendRequest.body())
                    .campaignRecipient(notificationSendRequest.campaignRecipient())
                    .personIds(Arrays.toString(notificationSendRequest.personIds()
                        .toArray()))
                    .build())
            .execute();

        NotificationFlowResponse notificationFlowResponse = flowStrategy.sendNotification(
            notificationSendRequest);

        if (notificationFlowResponse == null) {
            log.warn(
                "No notification flow response received, it's understood that nothing was done.");
            return;
        }

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

    @Builder
    public record Request(@NotNull NotificationSendRequest notificationSendRequest) {

    }
}
