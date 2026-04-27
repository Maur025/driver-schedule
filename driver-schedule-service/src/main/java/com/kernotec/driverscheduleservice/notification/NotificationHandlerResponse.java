package com.kernotec.driverscheduleservice.notification;

import java.util.List;
import lombok.Builder;

@Builder
public record NotificationHandlerResponse(List<SendResponse> responses, int successCount,
                                          int failureCount, boolean allSuccess)
{

}
