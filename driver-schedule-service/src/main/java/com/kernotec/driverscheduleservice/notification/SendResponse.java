package com.kernotec.driverscheduleservice.notification;

import com.kernotec.driverscheduleservice.notification.enums.NotificationErrorCode;
import lombok.Builder;

@Builder
public record SendResponse(String messageId, Exception exception, boolean isSuccessful,
                           NotificationErrorCode notificationErrorCode)
{

}
