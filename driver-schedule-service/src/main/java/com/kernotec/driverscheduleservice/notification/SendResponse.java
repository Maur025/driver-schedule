package com.kernotec.driverscheduleservice.notification;

import lombok.Builder;

@Builder
public record SendResponse(String messageId, Exception exception, boolean isSuccessful) {

}
