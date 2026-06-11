package com.kernotec.driverschedule.service.scheduling.notification;

import com.kernotec.driverschedule.notification.push.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import com.kernotec.driverschedule.service.scheduling.common.notification.NotificationTemplate.DriverAssignmentCancelledTemplate;
import com.kernotec.driverschedule.service.scheduling.common.notification.NotificationTemplate.DriverAssignmentTemplate;
import com.kernotec.driverschedule.service.scheduling.common.notification.PushNotificationCommon;
import com.kernotec.driverschedule.service.scheduling.command.schedule.ScheduleTransportationGetDtoCmd;
import com.kernotec.driverschedule.service.scheduling.jpa.dto.ScheduleTransportationDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class DriverAssignmentPushNotification extends PushNotificationCommon {

    private final NotificationOrchestrator notificationOrchestrator;
    private final ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd;

    public DriverAssignmentPushNotification(MessageSource messageSource,
        NotificationOrchestrator notificationOrchestrator,
        ScheduleTransportationGetDtoCmd scheduleTransportationGetDtoCmd)
    {
        super(messageSource);
        this.notificationOrchestrator = notificationOrchestrator;
        this.scheduleTransportationGetDtoCmd = scheduleTransportationGetDtoCmd;
    }

    public void onAssignmentTo(UUID scheduleId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(DriverAssignmentTemplate.TITLE, List.of()))
            .body(getMessage(DriverAssignmentTemplate.BODY, params))
            .campaignRecipient(DriverAssignmentTemplate.RECEIVER)
            .dataMap(getMapWithValues(DriverAssignmentTemplate.MAP_DATA, scheduleId))
            .personIds(personIds)
            .build());
    }

    public void onCancelled(UUID scheduleId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(scheduleId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(DriverAssignmentCancelledTemplate.TITLE, List.of()))
            .body(getMessage(DriverAssignmentCancelledTemplate.BODY, params))
            .campaignRecipient(DriverAssignmentCancelledTemplate.RECEIVER)
            .dataMap(getMapWithValues(DriverAssignmentCancelledTemplate.MAP_DATA, scheduleId))
            .personIds(personIds)
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
