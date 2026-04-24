package com.kernotec.driverscheduleservice.notification;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NotificationHandler {

    NotificationHandlerResponse pushNotification(@Valid NotificationHandlerRequest request);
}
