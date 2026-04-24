package com.kernotec.driverscheduleservice.notification;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationOrchestrator {

    private final NotificationHandler notificationHandler;

    @Async("notificationExecutor")
    public void sendAsyncNotification() {

        notificationHandler.pushNotification(NotificationHandlerRequest.builder()
            .tokens(Set.of(
                "evPe45YP1S5RoWmyyi8bIl:APA91bGr_S-YRXHgS60bmjlWSIzYO8Wz_6TslngzWWxyFcWQZE6e3DY2tDO8zAHQtiu0ISQPtuTQzfaouH0fZM9aDi6O3Y7epRPJMmsmsN_rGAFA_0EUsoA"))
            .title("Prueba Notificacion con abstraccion")
            .body("Nueba prueba de notificacion con abstraccion")
            .build());
    }
}
