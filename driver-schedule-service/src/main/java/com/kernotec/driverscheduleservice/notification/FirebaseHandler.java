package com.kernotec.driverscheduleservice.notification;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseHandler implements NotificationHandler {

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

        BatchResponse response = send(tokens, notification);

        return NotificationHandlerResponse.builder()
            .allSuccess(response.getFailureCount() == 0)
            .successCount(response.getSuccessCount())
            .failureCount(response.getFailureCount())
            .responses(getSendResponses(response.getResponses()))
            .build();
    }

    private BatchResponse send(Set<String> tokens, Notification notification) {
        MulticastMessage message = MulticastMessage.builder()
            .addAllTokens(tokens)
            .setNotification(notification)
            .setAndroidConfig(AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(30)
                    .toMillis())
                .setNotification(AndroidNotification.builder()
                    .setClickAction("OPEN_BOOKING_APP")
                    .setChannelId("kerno-booking-channel")
                    .build())
                .build())
            .setApnsConfig(ApnsConfig.builder()
                .setAps(Aps.builder()
                    .setBadge(1)
                    .setSound("default")
                    .build())
                .build())
            .setWebpushConfig(WebpushConfig.builder()
                .putHeader("Urgency", "high")
                .build())
            .build();

        try {
            return firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException ex) {
            log.error("Error while sending many notifications", ex);
            throw new RuntimeException(ex);
        }
    }

    private List<SendResponse> getSendResponses(
        List<com.google.firebase.messaging.SendResponse> responseList)
    {
        return responseList.stream()
            .map(externalResponse -> SendResponse.builder()
                .messageId(externalResponse.getMessageId())
                .exception(externalResponse.getException())
                .isSuccessful(externalResponse.isSuccessful())
                .build())
            .toList();
    }
}
