package com.kernotec.driverschedule.notification.rest.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationHandlerResponse(List<SendResponse> responses, int successCount,
                                          int failureCount, boolean allSuccess)
{

}
