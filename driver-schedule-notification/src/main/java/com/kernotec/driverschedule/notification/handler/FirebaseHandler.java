package com.kernotec.driverschedule.notification.handler;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.kernotec.driverschedule.notification.jpa.enums.NotificationErrorCode;
import com.kernotec.driverschedule.notification.rest.dto.request.NotificationHandlerRequest;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationHandlerResponse;
import com.kernotec.driverschedule.notification.rest.dto.response.SendResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseHandler implements NotificationHandler {

    private static final long EXPIRATION_IN_MINUTES = 60L;

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public NotificationHandlerResponse pushNotification(NotificationHandlerRequest request) {
        Set<String> tokens = request.tokens();

        Notification notification = Notification.builder()
            .setTitle(request.title())
            .setBody(request.body())
            .build();

        if (tokens.isEmpty()) {
            log.debug("No tokens found");
            return null;
        }

        BatchResponse response = send(tokens, notification, getDataMap(request));

        return NotificationHandlerResponse.builder()
            .allSuccess(response.getFailureCount() == 0)
            .successCount(response.getSuccessCount())
            .failureCount(response.getFailureCount())
            .responses(getSendResponses(response.getResponses()))
            .build();
    }

    private Map<String, String> getDataMap(NotificationHandlerRequest request) {
        Map<String, String> data = request.dataMap() == null ? new HashMap<>() : request.dataMap();

        String screen = data.get("screen");

        data.put("title", screen);
        data.put("body", request.body());

        return data;
    }

    private BatchResponse send(Set<String> tokens, Notification notification,
        Map<String, String> dataMap)
    {
        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setNotification(notification)
            .putAllData(dataMap)
            .setAndroidConfig(getAndroidConfig())
            .setApnsConfig(getApnsConfig())
            .setWebpushConfig(getWebpushConfig())
            .build();

        try {
            return firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException ex) {
            log.error("Error while sending many notifications", ex);
            throw new RuntimeException(ex);
        }
    }

    private AndroidConfig getAndroidConfig() {
        long androidExpiration = getExpirationInMilliseconds();

        return AndroidConfig.builder()
            .setTtl(androidExpiration)
            .setPriority(Priority.HIGH)
            .setNotification(AndroidNotification.builder()
                .setChannelId("kerno-booking-channel")
                .setDefaultSound(true)
                .build())
            .build();
    }

    private ApnsConfig getApnsConfig() {
        long iosExpirationLong = (System.currentTimeMillis() / 1000) + getExpirationInSeconds();
        String iosExpiration = String.valueOf(iosExpirationLong);

        return ApnsConfig.builder()
            .putHeader("apns-expiration", iosExpiration)
            .setAps(Aps.builder()
                .setContentAvailable(true)
                .setBadge(1)
                .setSound("default")
                .setThreadId("kerno-booking-thread")
                .build())
            .build();
    }

    private WebpushConfig getWebpushConfig() {
        String webExpiration = String.valueOf(getExpirationInSeconds());

        return WebpushConfig.builder()
            .putHeader("TTL", webExpiration)
            .putHeader("Urgency", "high")
            .build();
    }

    private long getExpirationInMilliseconds() {
        return EXPIRATION_IN_MINUTES * 60L * 1000L;
    }

    private long getExpirationInSeconds() {
        return EXPIRATION_IN_MINUTES * 60L;
    }

    private List<SendResponse> getSendResponses(
        List<com.google.firebase.messaging.SendResponse> responseList)
    {
        return responseList.stream()
            .map(externalResponse -> SendResponse.builder()
                .messageId(externalResponse.getMessageId())
                .exception(externalResponse.getException())
                .notificationErrorCode(getNotificationErrorCode(externalResponse))
                .isSuccessful(externalResponse.isSuccessful())
                .build())
            .toList();
    }

    private NotificationErrorCode getNotificationErrorCode(
        com.google.firebase.messaging.SendResponse externalResponse)
    {
        if (externalResponse.isSuccessful()) {
            return null;
        }

        FirebaseMessagingException exception = externalResponse.getException();

        if (exception == null) {
            return NotificationErrorCode.INTERNAL_SERVER_ERROR;
        }

        MessagingErrorCode messagingErrorCode = exception.getMessagingErrorCode();

        if (messagingErrorCode == null) {
            return NotificationErrorCode.INTERNAL_SERVER_ERROR;
        }

        return NotificationErrorCode.fromValue(messagingErrorCode.toString());
    }
}