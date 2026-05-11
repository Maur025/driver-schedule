package com.kernotec.driverschedule.service.schedule.notification;

import com.kernotec.driverschedule.notification.push.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.ScheduleApprovedTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.ScheduleCancelledTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.ScheduleFinalizedTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.ScheduleRescheduleTemplate;
import com.kernotec.driverschedule.service.common.notification.PushNotificationCommon;
import com.kernotec.driverschedule.service.schedule.command.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.schedule.jpa.dto.ScheduleTransportationDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class SchedulePushNotification extends PushNotificationCommon {

    private final NotificationOrchestrator notificationOrchestrator;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;

    public SchedulePushNotification(MessageSource messageSource,
        NotificationOrchestrator notificationOrchestrator,
        ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd)
    {
        super(messageSource);
        this.notificationOrchestrator = notificationOrchestrator;
        this.scheduleTransportationGetDtoCmd = scheduleTransportationGetDtoCmd;
    }

    public void onApproved(UUID scheduleId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(ScheduleApprovedTemplate.TITLE, List.of()))
            .body(getMessage(ScheduleApprovedTemplate.BODY, params))
            .campaignRecipient(ScheduleApprovedTemplate.RECEIVER)
            .dataMap(getMapWithValues(ScheduleApprovedTemplate.MAP_DATA, scheduleId))
            .personIds(personIds)
            .build());
    }

    public void onReschedule(UUID scheduleId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(ScheduleRescheduleTemplate.TITLE, List.of()))
            .body(getMessage(ScheduleRescheduleTemplate.BODY, params))
            .campaignRecipient(ScheduleRescheduleTemplate.RECEIVER)
            .dataMap(getMapWithValues(ScheduleRescheduleTemplate.MAP_DATA, scheduleId))
            .personIds(personIds)
            .build());
    }

    public void onCancelled(UUID scheduleId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(ScheduleCancelledTemplate.TITLE, List.of()))
            .body(getMessage(ScheduleCancelledTemplate.BODY, params))
            .campaignRecipient(ScheduleCancelledTemplate.RECEIVER)
            .dataMap(getMapWithValues(ScheduleCancelledTemplate.MAP_DATA, scheduleId))
            .personIds(personIds)
            .build());
    }

    public void onFinalized(UUID scheduleId) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(ScheduleFinalizedTemplate.TITLE, List.of()))
            .body(getMessage(ScheduleFinalizedTemplate.BODY, params))
            .campaignRecipient(ScheduleFinalizedTemplate.RECEIVER)
            .dataMap(getMapWithValues(ScheduleFinalizedTemplate.MAP_DATA, scheduleId))
            .build());
    }

    private Long getCorrelative(UUID scheduleId) {
        ScheduleTransportationDto scheduleDto = scheduleTransportationGetDtoCmd.withRequest(
                ScheduleTransportationGetDtoCmd.Request.builder()
                    .scheduleTransportationId(scheduleId)
                    .build())
            .execute();

        return scheduleDto.getTransportationRequest()
            .getCorrelative();
    }
}
