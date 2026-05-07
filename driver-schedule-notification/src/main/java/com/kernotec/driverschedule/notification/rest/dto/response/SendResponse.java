package com.kernotec.driverschedule.notification.rest.dto.response;

import com.kernotec.driverschedule.notification.notification.enums.NotificationErrorCode;
import lombok.Builder;

@Builder
public record SendResponse(String messageId, Exception exception, boolean isSuccessful,
                           NotificationErrorCode notificationErrorCode)
{

}
