package com.kernotec.driverschedule.service.trip.notification;

import com.kernotec.driverschedule.notification.push.NotificationOrchestrator;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationSendRequest;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.TripEmergencyDissmisedTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.TripEmergencyHandledTemplate;
import com.kernotec.driverschedule.service.common.notification.NotificationTemplate.TripEmergencyReportedTemplate;
import com.kernotec.driverschedule.service.common.notification.PushNotificationCommon;
import com.kernotec.driverschedule.service.trip.command.TripEmergencyGetDtoCmd;
import com.kernotec.driverschedule.service.trip.jpa.dto.TripEmergencyDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class TripEmergencyPushNotification extends PushNotificationCommon {

    private final NotificationOrchestrator notificationOrchestrator;
    private final TripEmergencyGetDtoCmd tripEmergencyGetDtoCmd;

    public TripEmergencyPushNotification(MessageSource messageSource,
        NotificationOrchestrator notificationOrchestrator,
        TripEmergencyGetDtoCmd tripEmergencyGetDtoCmd)
    {
        super(messageSource);
        this.notificationOrchestrator = notificationOrchestrator;
        this.tripEmergencyGetDtoCmd = tripEmergencyGetDtoCmd;
    }

    public void onReported(UUID tripEmergencyId) {
        List<Object> params = List.of(getCorrelative(tripEmergencyId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(TripEmergencyReportedTemplate.TITLE, List.of()))
            .body(getMessage(TripEmergencyReportedTemplate.BODY, params))
            .campaignRecipient(TripEmergencyReportedTemplate.RECEIVER)
            .dataMap(getMapWithValues(TripEmergencyReportedTemplate.MAP_DATA, tripEmergencyId))
            .build());
    }

    public void onDismissed(UUID tripEmergencyId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(tripEmergencyId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(TripEmergencyDissmisedTemplate.TITLE, List.of()))
            .body(getMessage(TripEmergencyDissmisedTemplate.BODY, params))
            .campaignRecipient(TripEmergencyDissmisedTemplate.RECEIVER)
            .dataMap(getMapWithValues(TripEmergencyDissmisedTemplate.MAP_DATA, tripEmergencyId))
            .personIds(personIds)
            .build());
    }

    public void onHandled(UUID tripEmergencyId, Set<UUID> personIds) {
        List<Object> params = List.of(getCorrelative(tripEmergencyId));

        notificationOrchestrator.sendAsyncNotification(NotificationSendRequest.builder()
            .title(getMessage(TripEmergencyHandledTemplate.TITLE, List.of()))
            .body(getMessage(TripEmergencyHandledTemplate.BODY, params))
            .campaignRecipient(TripEmergencyHandledTemplate.RECEIVER)
            .dataMap(getMapWithValues(TripEmergencyHandledTemplate.MAP_DATA, tripEmergencyId))
            .personIds(personIds)
            .build());
    }

    private Long getCorrelative(UUID tripEmergencyId) {
        TripEmergencyDto tripEmergencyDto = tripEmergencyGetDtoCmd.withRequest(
                TripEmergencyGetDtoCmd.Request.builder()
                    .tripEmergencyId(tripEmergencyId)
                    .build())
            .execute();

        return tripEmergencyDto.getScheduleTransportation()
            .getTransportationRequest()
            .getCorrelative();
    }
}
