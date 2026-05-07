package com.kernotec.driverschedule.service.notification;

import com.kernotec.driverschedule.service.notification.enums.NotificationErrorCode;
import lombok.Builder;

@Builder
public record SendResponse(String messageId, Exception exception, boolean isSuccessful,
                           NotificationErrorCode notificationErrorCode)
{

}
