package com.kernotec.driverschedule.notification.rest.dto.response;

import com.kernotec.driverschedule.notification.jpa.enums.NotificationErrorCode;
import lombok.Builder;

@Builder
public record SendResponse(String messageId, Exception exception, boolean isSuccessful,
                           NotificationErrorCode notificationErrorCode)
{

}
