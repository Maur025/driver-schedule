package com.kernotec.driverschedule.notification.handler;

import com.kernotec.driverschedule.notification.rest.dto.request.NotificationHandlerRequest;
import com.kernotec.driverschedule.notification.rest.dto.response.NotificationHandlerResponse;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface NotificationHandler {

    NotificationHandlerResponse pushNotification(@Valid NotificationHandlerRequest request);
}
