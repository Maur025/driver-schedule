package com.kernotec.driverschedule.service.request.notification;

import com.kernotec.driverschedule.notification.push.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.RequestCancelledTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.RequestCreateTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.RequestRejectedTemplate;
import com.kernotec.driverschedule.service.common.notification.PushNotificationCommon;
import com.kernotec.driverschedule.service.request.command.TransportationRequestGetDtoCmd;
import com.kernotec.driverschedule.service.request.jpa.dto.TransportationRequestDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class RequestPushNotification extends PushNotificationCommon {

    private final NotificationOrchestrator notificationOrchestrator;
    private final TransportationRequestGetDtoCmd transportationRequestGetDtoCmd;

    public RequestPushNotification(MessageSource messageSource,
        NotificationOrchestrator notificationOrchestrator,
        TransportationRequestGetDtoCmd transportationRequestGetDtoCmd)
    {
        super(messageSource);
        this.notificationOrchestrator = notificationOrchestrator;
        this.transportationRequestGetDtoCmd = transportationRequestGetDtoCmd;
    }

    public void onCreate(UUID requestId) {
        List<Object> params = List.of(getCorrelative(requestId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(RequestCreateTemplate.TITLE, List.of()))
            .body(getMessage(RequestCreateTemplate.BODY, params))
            .campaignRecipient(RequestCreateTemplate.RECEIVER)
            .dataMap(getMapWithValues(RequestCreateTemplate.MAP_DATA, requestId))
            .build());
    }

    public void onRejected(UUID requestId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(requestId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(RequestRejectedTemplate.TITLE, List.of()))
            .body(getMessage(RequestRejectedTemplate.BODY, params))
            .campaignRecipient(RequestRejectedTemplate.RECEIVER)
            .dataMap(getMapWithValues(RequestRejectedTemplate.MAP_DATA, requestId))
            .personIds(personIds)
            .build());
    }

    public void onCancelled(UUID requestId) {
        List<Object> params = List.of(getCorrelative(requestId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(RequestCancelledTemplate.TITLE, List.of()))
            .body(getMessage(RequestCancelledTemplate.BODY, params))
            .campaignRecipient(RequestCancelledTemplate.RECEIVER)
            .dataMap(getMapWithValues(RequestCancelledTemplate.MAP_DATA, requestId))
            .build());
    }

    private Long getCorrelative(UUID requestId) {
        TransportationRequestDto requestDto = transportationRequestGetDtoCmd.withRequest(
                TransportationRequestGetDtoCmd.Request.builder()
                    .transportationRequestId(requestId)
                    .build())
            .execute();

        return requestDto.getCorrelative();
    }
}
